package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.dto.LotteryRequest;
import com.eden.lottery.dto.LotteryResult;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.Prize;
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
 * 抽奖API控制器
 */
@RestController
@RequestMapping("/api")
public class LotteryController {
    
    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);
    
    @Autowired
    private LotteryService lotteryService;
    
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
