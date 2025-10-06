package com.eden.lottery.controller;

import com.eden.lottery.service.UserRoamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户漫游系统控制器
 */
@RestController
@RequestMapping("/api/user-roaming")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRoamingController {

    private static final Logger logger = LoggerFactory.getLogger(UserRoamingController.class);

    @Autowired
    private UserRoamingService userRoamingService;

    /**
     * 手动触发用户漫游（用于测试）
     */
    @PostMapping("/trigger")
    public Map<String, Object> triggerRoaming() {
        logger.info("接收到手动触发用户漫游请求");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            userRoamingService.manualTriggerRoaming();
            
            response.put("success", true);
            response.put("message", "用户漫游系统已手动触发");
            logger.info("手动触发用户漫游成功");
            
        } catch (Exception e) {
            logger.error("手动触发用户漫游失败: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "触发失败: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * 获取漫游系统状态
     */
    @GetMapping("/status")
    public Map<String, Object> getRoamingStatus() {
        logger.info("获取用户漫游系统状态");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = userRoamingService.getRoamingStats();
            
            response.put("success", true);
            response.put("data", stats);
            response.put("message", "获取漫游系统状态成功");
            
        } catch (Exception e) {
            logger.error("获取漫游系统状态失败: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "获取状态失败: " + e.getMessage());
        }
        
        return response;
    }
}
