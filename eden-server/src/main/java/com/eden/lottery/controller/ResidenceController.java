package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 居住地点控制器
 */
@RestController
@RequestMapping("/api/residence")
@CrossOrigin(origins = "*")
public class ResidenceController {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceController.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户当前居住地点
     */
    @GetMapping("/{userId}")
    public ApiResponse<Map<String, Object>> getUserResidence(@PathVariable String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.error("用户ID不能为空");
            }

            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("residence", user.getResidence());
            result.put("residenceName", getResidenceName(user.getResidence()));

            return ApiResponse.success("获取居住地点成功", result);
        } catch (Exception e) {
            logger.error("获取用户居住地点失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取居住地点失败: " + e.getMessage());
        }
    }

    /**
     * 设置用户居住地点
     */
    @PostMapping("/set")
    public ApiResponse<Map<String, Object>> setUserResidence(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String residence = request.get("residence");

            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.error("用户ID不能为空");
            }

            if (residence == null || residence.trim().isEmpty()) {
                return ApiResponse.error("居住地点不能为空");
            }

            // 验证居住地点是否有效
            if (!isValidResidence(residence)) {
                return ApiResponse.error("无效的居住地点");
            }

            // 检查用户是否存在
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            // 更新用户居住地点
            userMapper.updateResidence(userId, residence);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("residence", residence);
            result.put("residenceName", getResidenceName(residence));
            result.put("message", "居住地点设置成功！欢迎入住" + getResidenceName(residence) + "！");

            logger.info("用户 {} 设置居住地点为: {}", userId, residence);
            return ApiResponse.success("设置居住地点成功", result);
        } catch (Exception e) {
            logger.error("设置用户居住地点失败: {}", e.getMessage(), e);
            return ApiResponse.error("设置居住地点失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定地点的居住人员列表
     */
    @GetMapping("/residents/{residence}")
    public ApiResponse<Map<String, Object>> getResidenceResidents(@PathVariable String residence) {
        try {
            if (residence == null || residence.trim().isEmpty()) {
                return ApiResponse.error("居住地点不能为空");
            }

            // 验证居住地点是否有效
            if (!isValidResidence(residence)) {
                return ApiResponse.error("无效的居住地点");
            }

            // 获取该地点的所有居住者
            List<User> residents = userMapper.selectByResidence(residence);

            Map<String, Object> result = new HashMap<>();
            result.put("residence", residence);
            result.put("residenceName", getResidenceName(residence));
            result.put("residents", residents);
            result.put("residentCount", residents.size());

            logger.info("获取居住地点 {} 的居住者列表，共 {} 人", residence, residents.size());
            return ApiResponse.success("获取居住者列表成功", result);
        } catch (Exception e) {
            logger.error("获取居住者列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取居住者列表失败: " + e.getMessage());
        }
    }

    /**
     * 验证居住地点是否有效
     */
    private boolean isValidResidence(String residence) {
        return "castle".equals(residence) ||
               "city_hall".equals(residence) ||
               "palace".equals(residence) ||
               "dove_house".equals(residence) ||
               "park".equals(residence);
    }

    /**
     * 获取居住地点的中文名称
     */
    private String getResidenceName(String residence) {
        if (residence == null) {
            return "未选择";
        }
        
        switch (residence) {
            case "castle":
                return "城堡 🏰";
            case "city_hall":
                return "市政厅 🏛️";
            case "palace":
                return "行宫 🏯";
            case "dove_house":
                return "小白鸽家 🕊️";
            case "park":
                return "公园 🌳";
            default:
                return "未知地点";
        }
    }
}
