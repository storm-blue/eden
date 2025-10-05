package com.eden.lottery.controller;

import com.eden.lottery.dto.ResidenceEventItem;
import com.eden.lottery.entity.ResidenceEvent;
import com.eden.lottery.entity.User;
import com.eden.lottery.service.ResidenceEventService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 居所事件控制器
 */
@RestController
@RequestMapping("/api/residence-events")
@CrossOrigin(origins = "*")
public class ResidenceEventController {
    
    private static final Logger logger = LoggerFactory.getLogger(ResidenceEventController.class);
    
    @Autowired
    private ResidenceEventService residenceEventService;
    
    // Gson实例用于JSON序列化和反序列化
    private final Gson gson = new Gson();
    private final Type eventListType = new TypeToken<List<ResidenceEventItem>>(){}.getType();
    
    /**
     * 获取指定居所的当前事件
     * @param residence 居所类型
     * @return 事件信息
     */
    @GetMapping("/{residence}")
    public Map<String, Object> getResidenceEvent(@PathVariable String residence) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证居所类型
            if (!isValidResidence(residence)) {
                response.put("success", false);
                response.put("message", "无效的居所类型");
                return response;
            }
            
            // 获取居所事件
            ResidenceEvent event = residenceEventService.getResidenceEvent(residence);
            
            // 获取居所居住人员信息
            List<User> residents = residenceEventService.getResidenceResidents(residence);
            
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("residence", residence);
            eventData.put("residenceName", getResidenceName(residence));
            eventData.put("residents", residents);
            eventData.put("residentCount", residents != null ? residents.size() : 0);
            
                if (event != null) {
                    // 使用Gson解析事件数据
                    List<ResidenceEventItem> eventItems = parseEventDataWithGson(event.getEventData());
                    
                    // 转换为前端期望的格式
                    List<Map<String, Object>> events = new ArrayList<>();
                    for (ResidenceEventItem item : eventItems) {
                        Map<String, Object> eventMap = new HashMap<>();
                        eventMap.put("description", item.getDescription());
                        eventMap.put("type", item.getType());
                        eventMap.put("colors", item.getColors());
                        events.add(eventMap);
                    }
                    
                    eventData.put("events", events);
                    eventData.put("showHeartEffect", event.getShowHeartEffect());
                    eventData.put("specialText", event.getSpecialText());
                    eventData.put("showSpecialEffect", event.getShowSpecialEffect());
                    eventData.put("lastUpdated", event.getUpdatedAt());
                } else {
                    // 如果没有事件，返回默认值
                    List<Map<String, Object>> defaultEvents = new ArrayList<>();
                    Map<String, Object> defaultEvent = new HashMap<>();
                    defaultEvent.put("description", "暂无事件");
                    defaultEvent.put("type", "normal");
                    defaultEvent.put("colors", new String[]{"#888888", "#aaaaaa"});
                    defaultEvents.add(defaultEvent);
                    
                    eventData.put("events", defaultEvents);
                    eventData.put("showHeartEffect", false);
                    eventData.put("specialText", null);
                    eventData.put("showSpecialEffect", false);
                    eventData.put("lastUpdated", null);
                }
            
            response.put("success", true);
            response.put("message", "获取居所事件成功");
            response.put("data", eventData);
            
        } catch (Exception e) {
            logger.error("获取居所事件失败", e);
            response.put("success", false);
            response.put("message", "获取居所事件失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 手动刷新所有居所事件
     * @return 刷新结果
     */
    @PostMapping("/refresh")
    public Map<String, Object> refreshAllEvents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int successCount = residenceEventService.refreshAllResidenceEvents();
            
            response.put("success", true);
            response.put("message", "居所事件刷新完成");
            response.put("data", Map.of(
                "refreshedCount", successCount,
                "totalCount", 5
            ));
            
        } catch (Exception e) {
            logger.error("刷新居所事件失败", e);
            response.put("success", false);
            response.put("message", "刷新居所事件失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 手动更新指定居所的事件
     * @param residence 居所类型
     * @param requestBody 请求体
     * @return 更新结果
     */
    @PostMapping("/{residence}/update")
    public Map<String, Object> updateResidenceEvent(@PathVariable String residence, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证居所类型
            if (!isValidResidence(residence)) {
                response.put("success", false);
                response.put("message", "无效的居所类型");
                return response;
            }
            
            String eventData = (String) requestBody.get("eventData");
            Boolean showHeartEffect = (Boolean) requestBody.get("showHeartEffect");
            String specialText = (String) requestBody.get("specialText");
            Boolean showSpecialEffect = (Boolean) requestBody.get("showSpecialEffect");
            
            boolean success = residenceEventService.updateResidenceEvent(residence, eventData, showHeartEffect, specialText, showSpecialEffect);
            
            if (success) {
                response.put("success", true);
                response.put("message", "居所事件更新成功");
            } else {
                response.put("success", false);
                response.put("message", "居所事件更新失败");
            }
            
        } catch (Exception e) {
            logger.error("更新居所事件失败", e);
            response.put("success", false);
            response.put("message", "更新居所事件失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 验证居所类型是否有效
     */
    private boolean isValidResidence(String residence) {
        return residence != null && 
               (residence.equals("castle") || residence.equals("city_hall") || 
                residence.equals("palace") || residence.equals("dove_house") || 
                residence.equals("park"));
    }
    
    /**
     * 获取居所中文名称
     */
    private String getResidenceName(String residence) {
        switch (residence) {
            case "castle": return "🏰 城堡";
            case "city_hall": return "🏛️ 市政厅";
            case "palace": return "🏯 行宫";
            case "dove_house": return "🕊️ 小白鸽家";
            case "park": return "🌳 公园";
            default: return "未知居所";
        }
    }
    
    /**
     * 使用Gson解析事件数据JSON
     * @param eventDataJson JSON格式的事件数据
     * @return 解析后的事件列表
     */
    private List<ResidenceEventItem> parseEventDataWithGson(String eventDataJson) {
        if (eventDataJson == null || eventDataJson.trim().isEmpty() || eventDataJson.equals("[]")) {
            // 返回默认事件
            List<ResidenceEventItem> defaultEvents = new ArrayList<>();
            defaultEvents.add(new ResidenceEventItem("暂无事件", "normal"));
            return defaultEvents;
        }
        
        try {
            // 使用Gson反序列化
            List<ResidenceEventItem> events = gson.fromJson(eventDataJson, eventListType);
            
            if (events == null || events.isEmpty()) {
                // 如果解析结果为空，返回默认事件
                List<ResidenceEventItem> defaultEvents = new ArrayList<>();
                defaultEvents.add(new ResidenceEventItem("事件解析为空", "normal"));
                return defaultEvents;
            }
            
            // 确保每个事件都有正确的type和colors
            for (ResidenceEventItem event : events) {
                if (event.getType() == null) {
                    event.setType("normal");
                }
                if (event.getColors() == null) {
                    if ("special".equals(event.getType())) {
                        event.setColors(new String[]{"#ff69b4", "#ff1744"});
                    } else {
                        event.setColors(new String[]{"#888888", "#aaaaaa"});
                    }
                }
            }
            
            return events;
            
        } catch (Exception e) {
            logger.warn("使用Gson解析事件数据失败: {}", eventDataJson, e);
            // 返回错误事件
            List<ResidenceEventItem> errorEvents = new ArrayList<>();
            errorEvents.add(new ResidenceEventItem("事件数据格式错误", "normal", 
                new String[]{"#ff6b6b", "#feca57"}));
            return errorEvents;
        }
    }
}
