package com.eden.lottery.controller;

import com.eden.lottery.entity.Magic;
import com.eden.lottery.service.MagicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 魔法管理控制器
 */
@RestController
@RequestMapping("/api/magic")
public class MagicController {
    
    private static final Logger logger = LoggerFactory.getLogger(MagicController.class);
    
    @Autowired
    private MagicService magicService;
    
    /**
     * 获取所有魔法
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllMagics(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证权限
            if (!"秦小淮".equals(userId)) {
                response.put("success", false);
                response.put("message", "无权访问");
                return ResponseEntity.ok(response);
            }
            
            List<Magic> magics = magicService.getAllMagics();
            response.put("success", true);
            response.put("magics", magics);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("获取魔法列表失败", e);
            response.put("success", false);
            response.put("message", "获取魔法列表失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * 施展魔法
     */
    @PostMapping("/cast")
    public ResponseEntity<Map<String, Object>> castMagic(
            @RequestParam String code,
            @RequestParam String userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            magicService.castMagic(code, userId);
            
            // 获取更新后的魔法信息
            Magic magic = magicService.getMagicByCode(code);
            
            response.put("success", true);
            response.put("message", "魔法施展成功");
            response.put("remainingUses", magic.getRemainingUses());
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("施展魔法失败: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("施展魔法失败", e);
            response.put("success", false);
            response.put("message", "施展魔法失败: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}

