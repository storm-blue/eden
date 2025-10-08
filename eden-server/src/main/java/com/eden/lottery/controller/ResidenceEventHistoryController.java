package com.eden.lottery.controller;

import com.eden.lottery.entity.ResidenceEventHistory;
import com.eden.lottery.service.ResidenceEventService;
import com.eden.lottery.utils.ResidenceUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 居所事件历史API控制器
 */
@RestController
@RequestMapping("/api/residence-event-history")
public class ResidenceEventHistoryController {

    @Resource
    private ResidenceEventService residenceEventService;

    /**
     * 获取指定居所的事件历史（默认最近20条）
     */
    @GetMapping("/{residence}")
    public ResponseEntity<Map<String, Object>> getResidenceEventHistory(@PathVariable String residence) {
        try {
            List<ResidenceEventHistory> history = residenceEventService.getResidenceEventHistory(residence);
            Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "residence", residence,
                    "history", history,
                    "stats", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "获取事件历史失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取指定居所的事件历史（指定数量）
     */
    @GetMapping("/{residence}/{limit}")
    public ResponseEntity<Map<String, Object>> getResidenceEventHistoryWithLimit(
            @PathVariable String residence,
            @PathVariable int limit) {
        try {
            List<ResidenceEventHistory> history = residenceEventService.getResidenceEventHistory(residence, limit);
            Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "residence", residence,
                    "limit", limit,
                    "history", history,
                    "stats", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "获取事件历史失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 清理指定居所的所有事件历史
     */
    @DeleteMapping("/{residence}")
    public ResponseEntity<Map<String, Object>> clearResidenceEventHistory(@PathVariable String residence) {
        try {
            boolean success = residenceEventService.clearResidenceEventHistory(residence);

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "居所事件历史清理成功",
                        "residence", residence
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                        "success", false,
                        "message", "清理事件历史失败"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "清理事件历史失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取事件历史统计信息
     */
    @GetMapping("/{residence}/stats")
    public ResponseEntity<Map<String, Object>> getEventHistoryStats(@PathVariable String residence) {
        try {
            Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "获取统计信息失败: " + e.getMessage()
            ));
        }
    }

    /**
     * 获取所有居所的事件历史概览
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getAllResidenceEventHistoryOverview() {
        try {
            String[] residences = ResidenceUtils.getAllResidences();
            Map<String, Map<String, Object>> overview = new java.util.HashMap<>();

            for (String residence : residences) {
                Map<String, Object> stats = residenceEventService.getEventHistoryStats(residence);
                overview.put(residence, stats);
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "overview", overview
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "获取概览失败: " + e.getMessage()
            ));
        }
    }
}
