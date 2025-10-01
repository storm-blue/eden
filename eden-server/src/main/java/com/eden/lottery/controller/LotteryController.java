package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.dto.LotteryRequest;
import com.eden.lottery.dto.LotteryResult;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.Prize;
import com.eden.lottery.service.LotteryService;
import com.eden.lottery.service.UserService;
import com.eden.lottery.service.UserAttemptService;
import com.eden.lottery.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽奖API控制器
 */
@RestController
@RequestMapping("/api")
public class LotteryController {

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);

    @Autowired
    private LotteryService lotteryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserAttemptService userAttemptService;

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ApiResponse<Object> health() {
        return ApiResponse.success("Eden抽奖服务运行正常", new Object() {
            public final String message = "🎪 Eden抽奖服务运行正常";
            public final String timestamp = java.time.LocalDateTime.now().toString();
            public final String version = "2.0.0";
        });
    }

    /**
     * 获取奖品列表（不包含概率）
     */
    @GetMapping("/prizes")
    public ApiResponse<List<Object>> getPrizes() {
        try {
            List<Prize> prizes = lotteryService.getAllPrizes();
            List<Object> prizeList = prizes.stream()
                    .map(prize -> new Object() {
                        public final Long id = prize.getId();
                        public final String name = prize.getName();
                        public final String level = prize.getLevel();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(prizeList);
        } catch (Exception e) {
            logger.error("获取奖品列表失败", e);
            return ApiResponse.error("获取奖品列表失败");
        }
    }

    /**
     * 执行抽奖
     */
    @PostMapping("/lottery")
    public ApiResponse<LotteryResult> drawLottery(@RequestBody(required = false) LotteryRequest request,
                                                  HttpServletRequest httpRequest) {
        try {
            String userId = request != null && StringUtils.hasText(request.getUserId())
                    ? request.getUserId() : "anonymous";
            String ipAddress = getClientIpAddress(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");

            logger.info("用户 {} 开始抽奖，IP: {}", userId, ipAddress);

            LotteryResult result = lotteryService.drawLottery(userId, ipAddress, userAgent);
            return ApiResponse.success("抽奖成功", result);

        } catch (Exception e) {
            logger.error("抽奖失败", e);
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取抽奖记录
     */
    @GetMapping("/records/{userId}")
    public ApiResponse<List<Object>> getRecords(@PathVariable(required = false) String userId) {
        try {
            List<LotteryRecord> records;

            if ("all".equals(userId) || !StringUtils.hasText(userId)) {
                records = lotteryService.getRecentRecords();
            } else {
                records = lotteryService.getUserRecords(userId, 50);
            }

            List<Object> recordList = records.stream()
                    .map(record -> new Object() {
                        public final Long id = record.getId();
                        public final String prize = record.getPrize().getName();
                        public final String level = record.getPrize().getLevel();
                        public final String timestamp = record.getCreatedAt().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success(recordList);
        } catch (Exception e) {
            logger.error("获取抽奖记录失败", e);
            return ApiResponse.error("获取记录失败");
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Object> getStatistics() {
        try {
            Object stats = lotteryService.getStatistics();
            return ApiResponse.success(stats);
        } catch (Exception e) {
            logger.error("获取统计信息失败", e);
            return ApiResponse.error("获取统计信息失败");
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<Object> getUserInfo(@PathVariable String userId, HttpServletRequest httpRequest) {
        try {
            if (!StringUtils.hasText(userId)) {
                return ApiResponse.error("用户ID不能为空");
            }
            
            // 获取客户端信息
            String ipAddress = getClientIpAddress(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            
            User user = userService.getUserInfo(userId);
            boolean userExists = (user != null);
            
            // 记录用户尝试
            userAttemptService.recordAttempt(userId, userExists, ipAddress, userAgent);
            
            if (user == null) {
                // 用户不存在，返回默认信息
                final String finalUserId = userId; // 避免自引用
                Object userInfo = new Object() {
                    public final String userId = finalUserId;
                    public final Integer remainingDraws = 0;
                    public final Integer dailyDraws = 0;
                    public final String message = "用户不存在";
                };
                return ApiResponse.success("用户不存在", userInfo);
            }
            
            final User finalUser = user; // 避免自引用
            Object userInfo = new Object() {
                public final String userId = finalUser.getUserId();
                public final Integer remainingDraws = finalUser.getRemainingDraws();
                public final Integer dailyDraws = finalUser.getDailyDraws();
                public final String createTime = finalUser.getCreateTime().toString();
                public final String lastRefreshDate = finalUser.getLastRefreshDate().toString();
            };
            
            return ApiResponse.success("获取用户信息成功", userInfo);
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            return ApiResponse.error("获取用户信息失败");
        }
    }
    
    /**
     * 增加用户抽奖次数（抽到"再转一次"时使用）
     */
    @PostMapping("/user/{userId}/increase-draws")
    public ApiResponse<Object> increaseDraws(@PathVariable String userId, 
                                           @RequestParam(defaultValue = "1") Integer amount) {
        try {
            if (!StringUtils.hasText(userId)) {
                return ApiResponse.error("用户ID不能为空");
            }
            
            boolean success = userService.increaseRemainingDraws(userId, amount);
            if (success) {
                User user = userService.getUserInfo(userId);
                Object result = new Object() {
                    public final String userId = user.getUserId();
                    public final Integer remainingDraws = user.getRemainingDraws();
                    public final String message = "抽奖次数增加成功";
                };
                return ApiResponse.success("抽奖次数增加成功", result);
            } else {
                return ApiResponse.error("抽奖次数增加失败");
            }
        } catch (Exception e) {
            logger.error("增加用户抽奖次数失败", e);
            return ApiResponse.error("增加抽奖次数失败");
        }
    }

    /**
     * 首页信息
     */
    @GetMapping("/")
    public ApiResponse<Object> index() {
        return ApiResponse.success("🎪 欢迎来到Eden抽奖系统API服务 🎪", new Object() {
            public final String message = "🎪 欢迎来到Eden抽奖系统API服务 🎪";
            public final String version = "2.0.0-Java";
                public final Object endpoints = new Object() {
                    public final String prizes = "GET /api/prizes";
                    public final String lottery = "POST /api/lottery";
                    public final String records = "GET /api/records/{userId}";
                    public final String stats = "GET /api/stats";
                    public final String health = "GET /api/health";
                    public final String admin = "管理后台: /admin.html";
                };
        });
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多级代理的情况，取第一个IP
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
