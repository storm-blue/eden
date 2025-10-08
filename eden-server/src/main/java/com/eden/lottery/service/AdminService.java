package com.eden.lottery.service;

import com.eden.lottery.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理员服务
 */
@Service
public class AdminService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    
    // 固定的管理员账号
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin2008";
    
    // 简单的Session管理（实际项目中应该使用Redis或数据库）
    private final ConcurrentHashMap<String, Long> adminSessions = new ConcurrentHashMap<>();
    
    // Session过期时间：2小时
    private static final long SESSION_TIMEOUT = 2 * 60 * 60 * 1000L;
    
    private final UserService userService;
    
    public AdminService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 管理员登录
     */
    public String adminLogin(String username, String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            // 生成Session Token
            String token = UUID.randomUUID().toString();
            adminSessions.put(token, System.currentTimeMillis() + SESSION_TIMEOUT);
            
            logger.info("管理员登录成功，生成Token: {}", token.substring(0, 8) + "...");
            return token;
        } else {
            logger.warn("管理员登录失败，用户名: {}", username);
            return null;
        }
    }
    
    /**
     * 验证管理员Session
     */
    public boolean validateAdminSession(String token) {
        if (token == null) {
            return false;
        }
        
        Long expireTime = adminSessions.get(token);
        if (expireTime == null) {
            return false;
        }
        
        if (System.currentTimeMillis() > expireTime) {
            // Session过期，移除
            adminSessions.remove(token);
            logger.info("管理员Session过期: {}", token.substring(0, 8) + "...");
            return false;
        }
        
        return true;
    }
    
    /**
     * 管理员登出
     */
    public void adminLogout(String token) {
        if (token != null) {
            adminSessions.remove(token);
            logger.info("管理员登出: {}", token.substring(0, 8) + "...");
        }
    }
    
    /**
     * 获取所有用户列表（管理员功能）
     */
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * 给用户增加抽奖次数（管理员功能）
     */
    public boolean addUserDraws(String userId, Integer amount) {
        return userService.increaseRemainingDraws(userId, amount);
    }
    
    /**
     * 设置用户每日抽奖次数（管理员功能）
     */
    public boolean setUserDailyDraws(String userId, Integer dailyDraws) {
        return userService.updateUserDailyDraws(userId, dailyDraws);
    }
    
    /**
     * 添加新用户（管理员功能）
     */
    public User addUser(String userId, Integer dailyDraws) {
        if (dailyDraws == null || dailyDraws <= 0) {
            dailyDraws = 3; // 默认每日3次
        }
        
        // 检查用户是否已存在
        User existingUser = userService.getUserInfo(userId);
        if (existingUser != null) {
            // 用户已存在，更新每日抽奖次数
            userService.updateUserDailyDraws(userId, dailyDraws);
            logger.info("管理员更新现有用户 {} 的每日抽奖次数为: {}", userId, dailyDraws);
            return userService.getUserInfo(userId);
        } else {
            // 创建新用户
            logger.info("管理员创建新用户: {}，每日抽奖次数: {}", userId, dailyDraws);
            return userService.createUser(userId, dailyDraws);
        }
    }
    
    /**
     * 删除用户（管理员功能）
     */
    public boolean deleteUser(String userId) {
        boolean success = userService.deleteUser(userId);
        if (success) {
            logger.info("管理员删除用户: {}", userId);
        } else {
            logger.warn("管理员删除用户失败: {}", userId);
        }
        return success;
    }
    
    /**
     * 清理过期的Session
     */
    public void cleanupExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        adminSessions.entrySet().removeIf(entry -> currentTime > entry.getValue());
    }
    
    /**
     * 获取用户信息（管理员功能）
     */
    public User getUserById(String userId) {
        return userService.getUserById(userId);
    }
    
    /**
     * 更新用户简介（管理员功能）
     */
    public boolean updateUserProfile(String userId, String profile) {
        boolean success = userService.updateProfile(userId, profile);
        if (success) {
            logger.info("管理员更新用户 {} 的简介: {}", userId, profile);
        } else {
            logger.warn("管理员更新用户 {} 简介失败", userId);
        }
        return success;
    }
    
    /**
     * 更新用户状态（管理员功能）
     */
    public boolean updateUserStatus(String userId, String status) {
        boolean success = userService.updateStatus(userId, status);
        if (success) {
            logger.info("管理员更新用户 {} 的状态: {}", userId, status);
        } else {
            logger.warn("管理员更新用户 {} 状态失败", userId);
        }
        return success;
    }
    
    /**
     * 批量更新用户简介和状态（管理员功能）
     */
    public boolean updateUserProfileAndStatus(String userId, String profile, String status) {
        boolean success = userService.updateProfileAndStatus(userId, profile, status);
        if (success) {
            logger.info("管理员批量更新用户 {} 信息 - 简介: {}, 状态: {}", userId, profile, status);
        } else {
            logger.warn("管理员批量更新用户 {} 信息失败", userId);
        }
        return success;
    }
}
