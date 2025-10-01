package com.eden.lottery.controller;

import com.eden.lottery.dto.AdminLoginRequest;
import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.dto.UserManagementRequest;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.User;
import com.eden.lottery.service.AdminService;
import com.eden.lottery.service.LotteryService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理后台API控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final LotteryService lotteryService;
    
    public AdminController(AdminService adminService, LotteryService lotteryService) {
        this.adminService = adminService;
        this.lotteryService = lotteryService;
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ApiResponse<Object> login(@RequestBody AdminLoginRequest request) {
        try {
            String loginToken = adminService.adminLogin(request.getUsername(), request.getPassword());
            
            if (loginToken != null) {
                final String finalToken = loginToken; // 为匿名类创建final变量
                Object result = new Object() {
                    public final String token = finalToken;
                    public final String message = "登录成功";
                    public final Long expiresIn = 2 * 60 * 60L; // 2小时，单位秒
                };
                return ApiResponse.success("管理员登录成功", result);
            } else {
                return ApiResponse.error("用户名或密码错误");
            }
        } catch (Exception e) {
            logger.error("管理员登录失败", e);
            return ApiResponse.error("登录失败");
        }
    }

    /**
     * 管理员登出
     */
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        try {
            String token = getTokenFromRequest(request);
            adminService.adminLogout(token);
            return ApiResponse.success("登出成功");
        } catch (Exception e) {
            logger.error("管理员登出失败", e);
            return ApiResponse.error("登出失败");
        }
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/users")
    public ApiResponse<List<Object>> getUsers(HttpServletRequest request) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            List<User> users = adminService.getAllUsers();
            List<Object> userList = users.stream()
                    .map(user -> new Object() {
                        public final String userId = user.getUserId();
                        public final Integer remainingDraws = user.getRemainingDraws();
                        public final Integer dailyDraws = user.getDailyDraws();
                        public final String createTime = user.getCreateTime().toString();
                        public final String lastRefreshDate = user.getLastRefreshDate().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取用户列表成功", userList);
        } catch (Exception e) {
            logger.error("获取用户列表失败", e);
            return ApiResponse.error("获取用户列表失败");
        }
    }

    /**
     * 获取抽奖历史记录
     */
    @GetMapping("/lottery-history")
    public ApiResponse<List<Object>> getLotteryHistory(HttpServletRequest request,
                                                       @RequestParam(defaultValue = "100") int limit) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            List<LotteryRecord> records = lotteryService.getRecentRecords();
            List<Object> historyList = records.stream()
                    .limit(limit)
                    .map(record -> new Object() {
                        public final Long id = record.getId();
                        public final String userId = record.getUserId();
                        public final String prizeName = record.getPrize().getName();
                        public final String prizeLevel = record.getPrize().getLevel();
                        public final String ipAddress = record.getIpAddress();
                        public final String createTime = record.getCreatedAt().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取抽奖历史成功", historyList);
        } catch (Exception e) {
            logger.error("获取抽奖历史失败", e);
            return ApiResponse.error("获取抽奖历史失败");
        }
    }

    /**
     * 给用户增加抽奖次数
     */
    @PostMapping("/users/add-draws")
    public ApiResponse<Object> addUserDraws(HttpServletRequest request, 
                                           @RequestBody UserManagementRequest managementRequest) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (!StringUtils.hasText(managementRequest.getUserId()) || 
                managementRequest.getRemainingDraws() == null || 
                managementRequest.getRemainingDraws() <= 0) {
                return ApiResponse.error("参数错误");
            }

            boolean success = adminService.addUserDraws(
                managementRequest.getUserId(), 
                managementRequest.getRemainingDraws()
            );

            if (success) {
                Object result = new Object() {
                    public final String userId = managementRequest.getUserId();
                    public final Integer addedDraws = managementRequest.getRemainingDraws();
                    public final String message = "抽奖次数增加成功";
                };
                return ApiResponse.success("操作成功", result);
            } else {
                return ApiResponse.error("增加抽奖次数失败");
            }
        } catch (Exception e) {
            logger.error("增加用户抽奖次数失败", e);
            return ApiResponse.error("操作失败");
        }
    }

    /**
     * 设置用户每日抽奖次数
     */
    @PostMapping("/users/set-daily-draws")
    public ApiResponse<Object> setUserDailyDraws(HttpServletRequest request,
                                                @RequestBody UserManagementRequest managementRequest) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (!StringUtils.hasText(managementRequest.getUserId()) || 
                managementRequest.getDailyDraws() == null || 
                managementRequest.getDailyDraws() <= 0) {
                return ApiResponse.error("参数错误");
            }

            adminService.setUserDailyDraws(
                managementRequest.getUserId(), 
                managementRequest.getDailyDraws()
            );

            Object result = new Object() {
                public final String userId = managementRequest.getUserId();
                public final Integer dailyDraws = managementRequest.getDailyDraws();
                public final String message = "每日抽奖次数设置成功";
            };
            return ApiResponse.success("操作成功", result);
        } catch (Exception e) {
            logger.error("设置用户每日抽奖次数失败", e);
            return ApiResponse.error("操作失败");
        }
    }

    /**
     * 添加新用户
     */
    @PostMapping("/users/add")
    public ApiResponse<Object> addUser(HttpServletRequest request,
                                      @RequestBody UserManagementRequest managementRequest) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (!StringUtils.hasText(managementRequest.getUserId())) {
                return ApiResponse.error("用户ID不能为空");
            }

            User user = adminService.addUser(
                managementRequest.getUserId(), 
                managementRequest.getDailyDraws()
            );

            Object result = new Object() {
                public final String userId = user.getUserId();
                public final Integer remainingDraws = user.getRemainingDraws();
                public final Integer dailyDraws = user.getDailyDraws();
                public final String createTime = user.getCreateTime().toString();
                public final String message = "用户添加成功";
            };
            return ApiResponse.success("操作成功", result);
        } catch (Exception e) {
            logger.error("添加用户失败", e);
            return ApiResponse.error("操作失败");
        }
    }

    /**
     * 获取系统统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Object> getStats(HttpServletRequest request) {
        try {
            if (!validateAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            Object stats = lotteryService.getStatistics();
            List<User> users = adminService.getAllUsers();
            
            Object result = new Object() {
                public final Object lotteryStats = stats;
                public final int totalUsers = users.size();
                public final long totalActiveUsers = users.stream()
                    .filter(user -> user.getRemainingDraws() > 0)
                    .count();
            };
            
            return ApiResponse.success("获取统计信息成功", result);
        } catch (Exception e) {
            logger.error("获取统计信息失败", e);
            return ApiResponse.error("获取统计信息失败");
        }
    }

    /**
     * 验证管理员权限
     */
    private boolean validateAdmin(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return adminService.validateAdminSession(token);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
