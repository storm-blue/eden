package com.eden.lottery.controller;

import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.service.UserService;
import com.eden.lottery.utils.ResidenceUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息管理控制器
 * 用于管理用户的简介、状态等信息
 */
@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Resource
    private UserService userService;
    
    @Resource
    private UserMapper userMapper;

    /**
     * 获取用户详细信息
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("residence", user.getResidence());
            userInfo.put("residenceName", ResidenceUtils.getDisplayName(user.getResidence()));
            userInfo.put("avatarPath", user.getAvatarPath());
            userInfo.put("profile", user.getProfile() != null ? user.getProfile() : "这个人很神秘，什么都没有留下...");
            userInfo.put("status", user.getStatus() != null ? user.getStatus() : "安居乐业中");
            userInfo.put("remainingDraws", user.getRemainingDraws());
            userInfo.put("wishCount", user.getWishCount());
            userInfo.put("createTime", user.getCreateTime());

            response.put("success", true);
            response.put("userInfo", userInfo);
            
            logger.info("获取用户信息成功: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取用户信息失败: {}", userId, e);
            response.put("success", false);
            response.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新用户简介
     */
    @PostMapping("/{userId}/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @PathVariable String userId, 
            @RequestBody Map<String, String> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String profile = request.get("profile");
            if (profile == null) {
                response.put("success", false);
                response.put("message", "简介内容不能为空");
                return ResponseEntity.ok(response);
            }

            // 检查用户是否存在
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            // 更新用户简介
            userService.updateProfile(userId, profile);
            
            response.put("success", true);
            response.put("message", "简介更新成功");
            
            logger.info("用户简介更新成功: {} -> {}", userId, profile);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新用户简介失败: {}", userId, e);
            response.put("success", false);
            response.put("message", "更新简介失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新用户状态
     */
    @PostMapping("/{userId}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable String userId, 
            @RequestBody Map<String, String> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String status = request.get("status");
            if (status == null) {
                response.put("success", false);
                response.put("message", "状态内容不能为空");
                return ResponseEntity.ok(response);
            }

            // 检查用户是否存在
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            // 更新用户状态
            userService.updateStatus(userId, status);
            
            response.put("success", true);
            response.put("message", "状态更新成功");
            
            logger.info("用户状态更新成功: {} -> {}", userId, status);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("更新用户状态失败: {}", userId, e);
            response.put("success", false);
            response.put("message", "更新状态失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量更新用户简介和状态
     */
    @PostMapping("/{userId}/profile-status")
    public ResponseEntity<Map<String, Object>> updateProfileAndStatus(
            @PathVariable String userId, 
            @RequestBody Map<String, String> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String profile = request.get("profile");
            String status = request.get("status");
            
            if (profile == null && status == null) {
                response.put("success", false);
                response.put("message", "简介和状态不能都为空");
                return ResponseEntity.ok(response);
            }

            // 检查用户是否存在
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            // 批量更新用户简介和状态
            userService.updateProfileAndStatus(userId, profile, status);
            
            response.put("success", true);
            response.put("message", "信息更新成功");
            
            logger.info("用户信息批量更新成功: {} -> profile: {}, status: {}", userId, profile, status);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("批量更新用户信息失败: {}", userId, e);
            response.put("success", false);
            response.put("message", "更新信息失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 获取用户精力信息
     */
    @GetMapping("/{userId}/energy")
    public ResponseEntity<Map<String, Object>> getUserEnergy(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            // 获取用户精力信息
            Integer energy = userMapper.getUserEnergy(userId);
            
            Map<String, Object> energyInfo = new HashMap<>();
            energyInfo.put("energy", energy != null ? energy : 15);
            energyInfo.put("maxEnergy", user.getMaxEnergy() != null ? user.getMaxEnergy() : 15);
            energyInfo.put("energyRefreshTime", user.getEnergyRefreshTime());
            
            response.put("success", true);
            response.put("data", energyInfo);
            
            logger.info("获取用户精力信息成功: {}, 当前精力: {}", userId, energy);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取用户精力信息失败: {}", userId, e);
            response.put("success", false);
            response.put("message", "获取精力信息失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
