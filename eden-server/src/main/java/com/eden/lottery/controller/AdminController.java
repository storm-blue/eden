package com.eden.lottery.controller;

import com.eden.lottery.dto.AdminLoginRequest;
import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.dto.UserManagementRequest;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.User;
import com.eden.lottery.entity.Wish;
import com.eden.lottery.entity.ResidenceHistory;
import com.eden.lottery.service.AdminService;
import com.eden.lottery.service.LotteryService;
import com.eden.lottery.service.UserAttemptService;
import com.eden.lottery.service.WishService;
import com.eden.lottery.service.ResidenceHistoryService;
import com.eden.lottery.service.ResidenceEventService;
import com.eden.lottery.entity.UserAttempt;
import com.eden.lottery.utils.ResidenceUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    private final UserAttemptService userAttemptService;
    private final WishService wishService;
    private final ResidenceHistoryService residenceHistoryService;
    private final ResidenceEventService residenceEventService;

    public AdminController(AdminService adminService, LotteryService lotteryService, UserAttemptService userAttemptService, WishService wishService, ResidenceHistoryService residenceHistoryService, ResidenceEventService residenceEventService) {
        this.adminService = adminService;
        this.lotteryService = lotteryService;
        this.userAttemptService = userAttemptService;
        this.wishService = wishService;
        this.residenceHistoryService = residenceHistoryService;
        this.residenceEventService = residenceEventService;
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
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            List<User> users = adminService.getAllUsers();
            List<Object> userList = users.stream()
                    .map(user -> new Object() {
                        public final String userId = user.getUserId();
                        public final Integer remainingDraws = user.getRemainingDraws();
                        public final Integer dailyDraws = user.getDailyDraws();
                        public final String profile = user.getProfile();
                        public final String status = user.getStatus();
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
     * 获取抽奖历史记录（分页）
     */
    @GetMapping("/lottery-history")
    public ApiResponse<Object> getLotteryHistory(HttpServletRequest request,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "20") int size,
                                                 @RequestParam(required = false) String userId) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            // 计算偏移量
            int offset = (page - 1) * size;

            // 获取分页数据
            List<LotteryRecord> records = lotteryService.getLotteryHistoryWithPagination(userId, offset, size);
            long totalCount = lotteryService.getLotteryHistoryCount(userId);

            List<Object> historyList = records.stream()
                    .map(record -> new Object() {
                        public final Long id = record.getId();
                        public final String userId = record.getUserId();
                        public final String prizeName = record.getPrize() != null ? record.getPrize().getName() : "未知奖品";
                        public final String prizeLevel = record.getPrize() != null ? record.getPrize().getLevel() : "未知";
                        public final String ipAddress = record.getIpAddress();
                        public final String createTime = record.getCreatedAt().toString();
                    })
                    .collect(Collectors.toList());

            // 计算分页信息
            int totalPages = (int) Math.ceil((double) totalCount / size);
            boolean hasNext = page < totalPages;
            boolean hasPrev = page > 1;

            final int finalPage = page;
            final int finalSize = size;
            final long finalTotalCount = totalCount;
            final int finalTotalPages = totalPages;
            final boolean finalHasNext = hasNext;
            final boolean finalHasPrev = hasPrev;
            final String finalUserId = userId;

            Object result = new Object() {
                public final List<Object> records = historyList;
                public final Object pagination = new Object() {
                    public final int currentPage = finalPage;
                    public final int pageSize = finalSize;
                    public final long totalCount = finalTotalCount;
                    public final int totalPages = finalTotalPages;
                    public final boolean hasNext = finalHasNext;
                    public final boolean hasPrev = finalHasPrev;
                };
                public final Object filter = new Object() {
                    public final String userId = finalUserId;
                };
            };

            return ApiResponse.success("获取抽奖历史成功", result);
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
            if (isInvalidAdmin(request)) {
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
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (!StringUtils.hasText(managementRequest.getUserId()) ||
                    managementRequest.getDailyDraws() == null ||
                    managementRequest.getDailyDraws() <= 0) {
                return ApiResponse.error("参数错误");
            }

            boolean success = adminService.setUserDailyDraws(
                    managementRequest.getUserId(),
                    managementRequest.getDailyDraws()
            );

            if (success) {
                Object result = new Object() {
                    public final String userId = managementRequest.getUserId();
                    public final Integer dailyDraws = managementRequest.getDailyDraws();
                    public final String message = "每日抽奖次数设置成功";
                };
                return ApiResponse.success("操作成功", result);
            } else {
                return ApiResponse.error("用户不存在，请先创建用户");
            }
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
            if (isInvalidAdmin(request)) {
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
     * 删除用户
     */
    @DeleteMapping("/users/{userId}")
    public ApiResponse<Object> deleteUser(HttpServletRequest request,
                                          @PathVariable String userId) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (!StringUtils.hasText(userId)) {
                return ApiResponse.error("用户ID不能为空");
            }

            boolean success = adminService.deleteUser(userId);
            if (success) {
                final String finalUserId = userId; // 避免自引用
                Object result = new Object() {
                    public final String userId = finalUserId;
                    public final String message = "用户删除成功";
                };
                return ApiResponse.success("删除成功", result);
            } else {
                return ApiResponse.error("用户不存在或删除失败");
            }
        } catch (Exception e) {
            logger.error("删除用户失败", e);
            return ApiResponse.error("删除用户失败");
        }
    }

    /**
     * 获取系统统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Object> getStats(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            Object stats = lotteryService.getStatistics();
            Object attemptStats = userAttemptService.getAttemptStatistics();
            List<User> users = adminService.getAllUsers();

            Object result = new Object() {
                public final Object lotteryStats = stats;
                public final Object attemptStats = AdminController.this.userAttemptService.getAttemptStatistics();
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
     * 获取用户尝试记录
     */
    @GetMapping("/user-attempts")
    public ApiResponse<List<Object>> getUserAttempts(HttpServletRequest request,
                                                     @RequestParam(defaultValue = "100") Integer limit,
                                                     @RequestParam(required = false) String userId,
                                                     @RequestParam(defaultValue = "24") Integer hours) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            List<UserAttempt> attempts;

            if (StringUtils.hasText(userId)) {
                // 查询特定用户的尝试记录
                attempts = userAttemptService.getAttemptsByUserId(userId, limit);
            } else if (hours > 0) {
                // 查询最近N小时的尝试记录
                attempts = userAttemptService.getRecentAttempts(hours, limit);
            } else {
                // 查询所有尝试记录
                attempts = userAttemptService.getAllAttempts(limit);
            }

            List<Object> attemptList = attempts.stream()
                    .map(attempt -> new Object() {
                        public final Long id = attempt.getId();
                        public final String attemptUserId = attempt.getAttemptUserId();
                        public final Boolean userExists = attempt.getUserExists();
                        public final String ipAddress = attempt.getIpAddress();
                        public final String userAgent = attempt.getUserAgent();
                        public final String attemptTime = attempt.getAttemptTime().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取用户尝试记录成功", attemptList);
        } catch (Exception e) {
            logger.error("获取用户尝试记录失败", e);
            return ApiResponse.error("获取用户尝试记录失败");
        }
    }

    /**
     * 获取用户尝试统计信息
     */
    @GetMapping("/user-attempts/stats")
    public ApiResponse<Object> getUserAttemptStats(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            Object stats = userAttemptService.getAttemptStatistics();
            return ApiResponse.success("获取用户尝试统计成功", stats);
        } catch (Exception e) {
            logger.error("获取用户尝试统计失败", e);
            return ApiResponse.error("获取用户尝试统计失败");
        }
    }

    /**
     * 清理旧的用户尝试记录
     */
    @DeleteMapping("/user-attempts/cleanup")
    public ApiResponse<Object> cleanupOldAttempts(HttpServletRequest request,
                                                  @RequestParam(defaultValue = "30") Integer days) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            java.time.LocalDateTime beforeTime = java.time.LocalDateTime.now().minusDays(days);
            int deletedCount = userAttemptService.cleanOldAttempts(beforeTime);

            final int finalDeletedCount = deletedCount;
            Object result = new Object() {
                public final int deletedCount = finalDeletedCount;
                public final String message = "清理完成，删除了 " + finalDeletedCount + " 条记录";
            };

            return ApiResponse.success("清理完成", result);
        } catch (Exception e) {
            logger.error("清理用户尝试记录失败", e);
            return ApiResponse.error("清理失败");
        }
    }

    /**
     * 获取所有愿望列表（管理员查看）
     */
    @GetMapping("/wishes")
    public ApiResponse<List<Object>> getAllWishes(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            List<Wish> wishes = wishService.getAllWishesForAdmin();
            List<Object> wishList = wishes.stream()
                    .map(wish -> new Object() {
                        public final Long id = wish.getId();
                        public final String userId = wish.getUserId();
                        public final String wishContent = wish.getWishContent();
                        public final Double starX = wish.getStarX();
                        public final Double starY = wish.getStarY();
                        public final Integer starSize = wish.getStarSize();
                        public final String createTime = wish.getCreateTime().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取愿望列表成功", wishList);
        } catch (Exception e) {
            logger.error("获取愿望列表失败", e);
            return ApiResponse.error("获取愿望列表失败");
        }
    }

    /**
     * 删除指定愿望
     */
    @DeleteMapping("/wishes/{wishId}")
    public ApiResponse<Object> deleteWish(HttpServletRequest request,
                                          @PathVariable Long wishId) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (wishId == null || wishId <= 0) {
                return ApiResponse.error("愿望ID不能为空");
            }

            // 先检查愿望是否存在
            Wish wish = wishService.getWishById(wishId);
            if (wish == null) {
                return ApiResponse.error("愿望不存在");
            }

            boolean success = wishService.deleteWish(wishId);
            if (success) {
                final Long finalWishId = wishId;
                final String finalUserId = wish.getUserId();
                final String finalWishContent = wish.getWishContent();
                Object result = new Object() {
                    public final Long wishId = finalWishId;
                    public final String userId = finalUserId;
                    public final String wishContent = finalWishContent;
                    public final String message = "愿望删除成功";
                };
                logger.info("管理员删除了愿望：ID={}, 用户={}, 内容={}", wishId, wish.getUserId(), wish.getWishContent());
                return ApiResponse.success("删除成功", result);
            } else {
                return ApiResponse.error("愿望删除失败");
            }
        } catch (Exception e) {
            logger.error("删除愿望失败", e);
            return ApiResponse.error("删除愿望失败");
        }
    }

    /**
     * 获取愿望统计信息
     */
    @GetMapping("/wishes/stats")
    public ApiResponse<Object> getWishStats(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            Object stats = wishService.getWishStatistics();
            return ApiResponse.success("获取愿望统计成功", stats);
        } catch (Exception e) {
            logger.error("获取愿望统计失败", e);
            return ApiResponse.error("获取愿望统计失败");
        }
    }

    /**
     * 获取所有居住历史记录（分页）
     */
    @GetMapping("/residence-history")
    public ApiResponse<Map<String, Object>> getAllResidenceHistory(HttpServletRequest request,
                                                                   @RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "20") int size) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 20;

            Map<String, Object> result = residenceHistoryService.getAllResidenceHistory(page, size);
            return ApiResponse.success("获取居住历史成功", result);
        } catch (Exception e) {
            logger.error("获取居住历史失败", e);
            return ApiResponse.error("获取居住历史失败");
        }
    }

    /**
     * 获取指定用户的居住历史
     */
    @GetMapping("/residence-history/user/{userId}")
    public ApiResponse<List<ResidenceHistory>> getUserResidenceHistory(HttpServletRequest request,
                                                                       @PathVariable String userId) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (userId == null || userId.trim().isEmpty()) {
                return ApiResponse.error("用户ID不能为空");
            }

            List<ResidenceHistory> history = residenceHistoryService.getUserResidenceHistory(userId);
            return ApiResponse.success("获取用户居住历史成功", history);
        } catch (Exception e) {
            logger.error("获取用户居住历史失败", e);
            return ApiResponse.error("获取用户居住历史失败");
        }
    }

    /**
     * 获取指定地点的居住历史
     */
    @GetMapping("/residence-history/location/{residence}")
    public ApiResponse<List<ResidenceHistory>> getLocationResidenceHistory(HttpServletRequest request,
                                                                           @PathVariable String residence) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (residence == null || residence.trim().isEmpty()) {
                return ApiResponse.error("居住地点不能为空");
            }

            List<ResidenceHistory> history = residenceHistoryService.getResidenceHistory(residence);
            return ApiResponse.success("获取地点居住历史成功", history);
        } catch (Exception e) {
            logger.error("获取地点居住历史失败", e);
            return ApiResponse.error("获取地点居住历史失败");
        }
    }

    /**
     * 获取居住历史统计信息
     */
    @GetMapping("/residence-history/stats")
    public ApiResponse<Map<String, Object>> getResidenceHistoryStats(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            Map<String, Object> stats = residenceHistoryService.getResidenceStatistics();
            return ApiResponse.success("获取居住历史统计成功", stats);
        } catch (Exception e) {
            logger.error("获取居住历史统计失败", e);
            return ApiResponse.error("获取居住历史统计失败");
        }
    }

    /**
     * 删除居住历史记录
     */
    @DeleteMapping("/residence-history/{historyId}")
    public ApiResponse<Object> deleteResidenceHistory(HttpServletRequest request,
                                                      @PathVariable Long historyId) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            if (historyId == null || historyId <= 0) {
                return ApiResponse.error("历史记录ID不能为空");
            }

            boolean success = residenceHistoryService.deleteResidenceHistory(historyId);
            if (success) {
                final Long finalHistoryId = historyId;
                Object result = new Object() {
                    public final Long historyId = finalHistoryId;
                    public final String message = "居住历史记录删除成功";
                };
                logger.info("管理员删除了居住历史记录：ID={}", historyId);
                return ApiResponse.success("删除成功", result);
            } else {
                return ApiResponse.error("居住历史记录删除失败");
            }
        } catch (Exception e) {
            logger.error("删除居住历史记录失败", e);
            return ApiResponse.error("删除居住历史记录失败");
        }
    }

    /**
     * 清除指定居所的所有事件历史记录
     */
    @DeleteMapping("/residence-event-history/{residence}")
    public ApiResponse<Object> clearResidenceEventHistory(@PathVariable String residence, HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            // 验证居所参数
            if (residence == null || residence.trim().isEmpty()) {
                return ApiResponse.error("居所参数不能为空");
            }

            // 验证居所是否有效
            if (ResidenceUtils.isInvalidResidence(residence)) {
                return ApiResponse.error("无效的居所类型");
            }

            // 获取居所显示名称
            String residenceName = ResidenceUtils.getDisplayName(residence);

            // 清除指定居所的事件历史
            boolean success = residenceEventService.clearResidenceEventHistory(residence);

            if (success) {
                final String finalResidence = residence;
                final String finalResidenceName = residenceName;
                Object result = new Object() {
                    public final String residence = finalResidence;
                    public final String residenceName = finalResidenceName;
                    public final String message = "居所事件历史清除成功";
                };

                logger.info("管理员清除了居所事件历史：{} ({})", residenceName, residence);
                return ApiResponse.success("清除成功", result);
            } else {
                return ApiResponse.error("清除居所事件历史失败");
            }
        } catch (Exception e) {
            logger.error("清除居所事件历史失败：residence={}", residence, e);
            return ApiResponse.error("清除居所事件历史失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有居所的事件历史统计信息
     */
    @GetMapping("/residence-event-history/overview")
    public ApiResponse<Object> getResidenceEventHistoryOverview(HttpServletRequest request) {
        try {
            if (isInvalidAdmin(request)) {
                return ApiResponse.error("未授权访问");
            }

            String[] residences = {"castle", "park", "city_hall", "white_dove_house", "palace"};
            Map<String, Object> overview = new java.util.HashMap<>();

            for (String residence : residences) {
                Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);

                // 创建一个新的可变Map来避免UnsupportedOperationException
                Map<String, Object> residenceStats = new java.util.HashMap<>(stats);
                residenceStats.put("residenceName", ResidenceUtils.getDisplayName(residence));

                overview.put(residence, residenceStats);
            }

            final Map<String, Object> finalOverview = overview;
            Object result = new Object() {
                public final Map<String, Object> overview = finalOverview;
                public final String message = "获取居所事件历史概览成功";
            };

            return ApiResponse.success("获取成功", result);
        } catch (Exception e) {
            logger.error("获取居所事件历史概览失败", e);
            return ApiResponse.error("获取居所事件历史概览失败：" + e.getMessage());
        }
    }

    /**
     * 验证管理员权限
     */
    private boolean isInvalidAdmin(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        return !adminService.validateAdminSession(token);
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

    /**
     * 更新用户简介
     */
    @PostMapping("/users/{userId}/profile")
    public Map<String, Object> updateUserProfile(
            @PathVariable String userId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        
        Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            // 验证管理员权限
            if (isInvalidAdmin(httpRequest)) {
                response.put("success", false);
                response.put("message", "无效的管理员会话");
                return response;
            }

            String profile = request.get("profile");
            if (profile == null) {
                response.put("success", false);
                response.put("message", "简介内容不能为空");
                return response;
            }

            // 检查用户是否存在
            User user = adminService.getUserById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            // 更新用户简介
            boolean success = adminService.updateUserProfile(userId, profile);
            if (success) {
                response.put("success", true);
                response.put("message", "用户简介更新成功");
                logger.info("管理员更新用户 {} 的简介: {}", userId, profile);
            } else {
                response.put("success", false);
                response.put("message", "更新用户简介失败");
            }
            
            return response;
            
        } catch (Exception e) {
            logger.error("管理员更新用户简介失败", e);
            response.put("success", false);
            response.put("message", "更新用户简介失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 更新用户状态
     */
    @PostMapping("/users/{userId}/status")
    public Map<String, Object> updateUserStatus(
            @PathVariable String userId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        
        Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            // 验证管理员权限
            if (isInvalidAdmin(httpRequest)) {
                response.put("success", false);
                response.put("message", "无效的管理员会话");
                return response;
            }

            String status = request.get("status");
            if (status == null) {
                response.put("success", false);
                response.put("message", "状态内容不能为空");
                return response;
            }

            // 检查用户是否存在
            User user = adminService.getUserById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            // 更新用户状态
            boolean success = adminService.updateUserStatus(userId, status);
            if (success) {
                response.put("success", true);
                response.put("message", "用户状态更新成功");
                logger.info("管理员更新用户 {} 的状态: {}", userId, status);
            } else {
                response.put("success", false);
                response.put("message", "更新用户状态失败");
            }
            
            return response;
            
        } catch (Exception e) {
            logger.error("管理员更新用户状态失败", e);
            response.put("success", false);
            response.put("message", "更新用户状态失败: " + e.getMessage());
            return response;
        }
    }

    /**
     * 批量更新用户简介和状态
     */
    @PostMapping("/users/{userId}/profile-status")
    public Map<String, Object> updateUserProfileAndStatus(
            @PathVariable String userId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        
        Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            // 验证管理员权限
            if (isInvalidAdmin(httpRequest)) {
                response.put("success", false);
                response.put("message", "无效的管理员会话");
                return response;
            }

            String profile = request.get("profile");
            String status = request.get("status");
            
            if (profile == null && status == null) {
                response.put("success", false);
                response.put("message", "简介和状态不能都为空");
                return response;
            }

            // 检查用户是否存在
            User user = adminService.getUserById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            // 批量更新用户简介和状态
            boolean success = adminService.updateUserProfileAndStatus(userId, profile, status);
            if (success) {
                response.put("success", true);
                response.put("message", "用户信息更新成功");
                logger.info("管理员批量更新用户 {} 信息 - 简介: {}, 状态: {}", userId, profile, status);
            } else {
                response.put("success", false);
                response.put("message", "更新用户信息失败");
            }
            
            return response;
            
        } catch (Exception e) {
            logger.error("管理员批量更新用户信息失败", e);
            response.put("success", false);
            response.put("message", "更新用户信息失败: " + e.getMessage());
            return response;
        }
    }

}
