package com.eden.lottery.controller;

import com.eden.lottery.entity.GiantAttack;
import com.eden.lottery.service.GiantAttackService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巨人进攻控制器
 */
@RestController
@RequestMapping("/api/giant-attack")
public class GiantAttackController {

    @Resource
    private GiantAttackService giantAttackService;

    /**
     * 获取当前巨人进攻状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getGiantAttackStatus() {
        try {
            GiantAttack currentAttack = giantAttackService.getCurrentGiantAttack();
            boolean isAttacking = giantAttackService.isGiantAttacking();
            
            Map<String, Object> data = new HashMap<>();
            data.put("isAttacking", isAttacking);
            
            if (currentAttack != null) {
                data.put("startTime", currentAttack.getStartTime());
                data.put("endTime", currentAttack.getEndTime());
                data.put("lastDamageTime", currentAttack.getLastDamageTime());
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", data);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取巨人进攻状态失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 手动触发巨人进攻（管理员功能）
     */
    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerGiantAttack(HttpServletRequest request) {
        try {
            // 检查管理员权限
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "需要管理员权限");
                return ResponseEntity.status(401).body(error);
            }

            giantAttackService.startGiantAttack();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "巨人进攻已触发");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "触发巨人进攻失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 手动结束巨人进攻（管理员功能）
     */
    @PostMapping("/end")
    public ResponseEntity<Map<String, Object>> endGiantAttack(HttpServletRequest request) {
        try {
            // 检查管理员权限
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "需要管理员权限");
                return ResponseEntity.status(401).body(error);
            }

            giantAttackService.endGiantAttack();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "巨人进攻已结束");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "结束巨人进攻失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 手动处理巨人伤害（管理员功能）
     */
    @PostMapping("/damage")
    public ResponseEntity<Map<String, Object>> processGiantDamage(HttpServletRequest request) {
        try {
            // 检查管理员权限
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "需要管理员权限");
                return ResponseEntity.status(401).body(error);
            }

            giantAttackService.processGiantDamage();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "巨人伤害已处理");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "处理巨人伤害失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * 获取巨人进攻历史记录（管理员功能）
     */
    @GetMapping("/admin/history")
    public ResponseEntity<Map<String, Object>> getGiantAttackHistory(HttpServletRequest request) {
        try {
            // 检查管理员权限
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "需要管理员权限");
                return ResponseEntity.status(401).body(error);
            }

            List<GiantAttack> history = giantAttackService.getAllGiantAttackHistory();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", history);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取巨人进攻历史失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
