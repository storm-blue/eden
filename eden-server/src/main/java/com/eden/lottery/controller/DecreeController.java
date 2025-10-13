package com.eden.lottery.controller;

import com.eden.lottery.entity.Decree;
import com.eden.lottery.service.DecreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令管理控制器
 */
@RestController
@RequestMapping("/api/decree")
public class DecreeController {
    
    private static final Logger logger = LoggerFactory.getLogger(DecreeController.class);
    
    @Autowired
    private DecreeService decreeService;
    
    /**
     * 获取所有命令
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllDecrees(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证权限
            if (!"秦小淮".equals(userId)) {
                response.put("success", false);
                response.put("message", "无权访问");
                return ResponseEntity.ok(response);
            }
            
            List<Decree> decrees = decreeService.getAllDecrees();
            response.put("success", true);
            response.put("decrees", decrees);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取命令列表失败", e);
            response.put("success", false);
            response.put("message", "获取命令列表失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 颁布命令
     */
    @PostMapping("/issue")
    public ResponseEntity<Map<String, Object>> issueDecree(
            @RequestParam String code,
            @RequestParam String userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            decreeService.issueDecree(code, userId);
            response.put("success", true);
            response.put("message", "命令已颁布");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("颁布命令失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("颁布命令失败", e);
            response.put("success", false);
            response.put("message", "颁布命令失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 取消命令
     */
    @PostMapping("/cancel")
    public ResponseEntity<Map<String, Object>> cancelDecree(
            @RequestParam String code,
            @RequestParam String userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            decreeService.cancelDecree(code, userId);
            response.put("success", true);
            response.put("message", "命令已取消");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("取消命令失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("取消命令失败", e);
            response.put("success", false);
            response.put("message", "取消命令失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}

