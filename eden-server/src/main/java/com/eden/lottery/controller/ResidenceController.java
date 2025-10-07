package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.entity.User;
import com.eden.lottery.entity.ResidenceHistory;
import com.eden.lottery.service.ResidenceHistoryService;
import com.eden.lottery.service.ResidenceEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private ResidenceHistoryService residenceHistoryService;

    @Autowired
    private ResidenceEventService residenceEventService;

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
    public ApiResponse<Map<String, Object>> setUserResidence(@RequestBody Map<String, String> request,
                                                           HttpServletRequest httpRequest) {
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

            // 获取之前的居住地点
            String previousResidence = user.getResidence();

            // 如果居住地点没有变化，不需要更新
            if (residence.equals(previousResidence)) {
                Map<String, Object> result = new HashMap<>();
                result.put("userId", userId);
                result.put("residence", residence);
                result.put("residenceName", getResidenceName(residence));
                result.put("message", "您已经居住在" + getResidenceName(residence) + "了！");
                return ApiResponse.success("设置居住地点成功", result);
            }

            // 更新用户居住地点
            userMapper.updateResidence(userId, residence);

            // 记录居住历史
            String ipAddress = getClientIpAddress(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            residenceHistoryService.recordResidenceChange(userId, residence, previousResidence, ipAddress, userAgent);

            // 刷新相关居所的事件
            refreshResidenceEvents(userId, residence, previousResidence);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("residence", residence);
            result.put("residenceName", getResidenceName(residence));
            result.put("previousResidence", previousResidence);
            result.put("previousResidenceName", getResidenceName(previousResidence));
            result.put("message", "居住地点设置成功！欢迎入住" + getResidenceName(residence) + "！");

            logger.info("用户 {} 从 {} 搬迁到 {}", userId, getResidenceName(previousResidence), getResidenceName(residence));
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
     * 获取用户的居住历史
     */
    @GetMapping("/history/{userId}")
    public ApiResponse<List<ResidenceHistory>> getUserResidenceHistory(@PathVariable String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.error("用户ID不能为空");
            }

            // 检查用户是否存在
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            List<ResidenceHistory> history = residenceHistoryService.getUserResidenceHistory(userId);
            logger.info("获取用户 {} 的居住历史，共 {} 条记录", userId, history.size());
            return ApiResponse.success("获取居住历史成功", history);
        } catch (Exception e) {
            logger.error("获取用户居住历史失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取居住历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定地点的居住历史
     */
    @GetMapping("/history/location/{residence}")
    public ApiResponse<List<ResidenceHistory>> getLocationResidenceHistory(@PathVariable String residence) {
        try {
            if (residence == null || residence.trim().isEmpty()) {
                return ApiResponse.error("居住地点不能为空");
            }

            // 验证居住地点是否有效
            if (!isValidResidence(residence)) {
                return ApiResponse.error("无效的居住地点");
            }

            List<ResidenceHistory> history = residenceHistoryService.getResidenceHistory(residence);
            logger.info("获取居住地点 {} 的历史记录，共 {} 条记录", residence, history.size());
            return ApiResponse.success("获取居住历史成功", history);
        } catch (Exception e) {
            logger.error("获取居住地点历史失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取居住历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取居住历史统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getResidenceStatistics() {
        try {
            Map<String, Object> stats = residenceHistoryService.getResidenceStatistics();
            return ApiResponse.success("获取居住统计成功", stats);
        } catch (Exception e) {
            logger.error("获取居住统计失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取居住统计失败: " + e.getMessage());
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

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 刷新相关居所的事件
     * 当用户移动时，需要刷新新居所和旧居所的事件
     */
    private void refreshResidenceEvents(String userId, String newResidence, String previousResidence) {
        try {
            // 为旧居所生成离开事件（用户搬出）
            if (previousResidence != null && !previousResidence.trim().isEmpty() && !previousResidence.equals(newResidence)) {
                generateDepartureEvent(userId, previousResidence);
                logger.info("已生成离开事件: {} 离开了 {}", userId, getResidenceName(previousResidence));
            }

            // 为新居所生成入住事件（用户搬入）
            if (newResidence != null && !newResidence.trim().isEmpty()) {
                generateArrivalEvent(userId, newResidence);
                logger.info("已生成入住事件: {} 入住了 {}", userId, getResidenceName(newResidence));
            }
        } catch (Exception e) {
            logger.error("刷新居所事件失败 - 用户: {}, 新居所: {}, 旧居所: {}, 错误: {}", 
                    userId, newResidence, previousResidence, e.getMessage(), e);
        }
    }

    /**
     * 生成离开事件
     */
    private void generateDepartureEvent(String username, String residence) {
        try {
            // 创建离开事件
            List<com.eden.lottery.dto.ResidenceEventItem> events = new java.util.ArrayList<>();
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                username + " 离开了" + getResidenceName(residence), "normal"));
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                getResidenceName(residence) + "变得安静了...", "normal"));
            
            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);
            
            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);
            
        } catch (Exception e) {
            logger.error("生成离开事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }

    /**
     * 生成入住事件
     */
    private void generateArrivalEvent(String username, String residence) {
        try {
            // 创建入住事件
            List<com.eden.lottery.dto.ResidenceEventItem> events = new java.util.ArrayList<>();
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                username + " 入住了" + getResidenceName(residence), "normal"));
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                getResidenceName(residence) + "迎来了新的住客", "normal"));
            
            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);
            
            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);
            
        } catch (Exception e) {
            logger.error("生成入住事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }
}
