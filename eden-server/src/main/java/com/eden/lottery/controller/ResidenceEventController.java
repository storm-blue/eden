package com.eden.lottery.controller;

import com.eden.lottery.event.EventItem;
import com.eden.lottery.entity.ResidenceEvent;
import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.service.ResidenceEventService;
import com.eden.lottery.utils.ResidenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource
    private ResidenceEventService residenceEventService;

    @Resource
    private UserMapper userMapper;

    // Gson实例用于JSON序列化和反序列化
    private final Gson gson = new Gson();
    private final Type eventListType = new TypeToken<List<EventItem>>() {
    }.getType();

    /**
     * 获取指定居所的当前事件
     *
     * @param residence 居所类型
     * @return 事件信息
     */
    @GetMapping("/{residence}")
    public Map<String, Object> getResidenceEvent(@PathVariable String residence) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证居所类型
            if (ResidenceUtils.isInvalidResidence(residence)) {
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
            eventData.put("residenceName", ResidenceUtils.getDisplayName(residence));
            eventData.put("residents", residents);
            eventData.put("residentCount", residents != null ? residents.size() : 0);

            if (event != null) {
                // 使用Gson解析事件数据
                List<EventItem> eventItems = parseEventDataWithGson(event.getEventData());

                // 转换为前端期望的格式
                List<Map<String, Object>> events = new ArrayList<>();
                for (EventItem item : eventItems) {
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
     *
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
     * 手动刷新指定居所的事件（搞点事情功能）
     *
     * @param residence 居所类型
     * @param requestBody 请求体，包含userId
     * @return 刷新结果
     */
    @PostMapping("/refresh/{residence}")
    public Map<String, Object> refreshResidenceEvent(
            @PathVariable String residence,
            @RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证居所类型
            if (ResidenceUtils.isInvalidResidence(residence)) {
                response.put("success", false);
                response.put("message", "无效的居所类型");
                return response;
            }

            // 获取用户ID
            String userId = requestBody.get("userId");
            if (userId == null || userId.isEmpty()) {
                response.put("success", false);
                response.put("message", "用户ID不能为空");
                return response;
            }

            // 查询用户信息
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            // 检查耐力值
            Integer stamina = user.getStamina();
            if (stamina == null || stamina <= 0) {
                response.put("success", false);
                response.put("message", "你已经精疲力尽了，歇会吧");
                response.put("stamina", 0);
                return response;
            }

            // 扣减耐力值
            userMapper.decreaseStamina(userId);

            // 刷新居所事件
            boolean success = residenceEventService.generateResidenceEvent(residence);

            if (success) {
                // 重新查询用户获取最新耐力值
                user = userMapper.selectByUserId(userId);
                response.put("success", true);
                response.put("message", "居所事件刷新成功");
                response.put("data", Map.of(
                        "residence", residence,
                        "residenceName", ResidenceUtils.getDisplayName(residence)
                ));
                response.put("stamina", user.getStamina());
                logger.info("用户 {} 搞了点事情，当前耐力: {}", userId, user.getStamina());
            } else {
                response.put("success", false);
                response.put("message", "居所事件刷新失败");
            }

        } catch (Exception e) {
            logger.error("刷新居所事件失败，居所: {}", residence, e);
            response.put("success", false);
            response.put("message", "刷新居所事件失败: " + e.getMessage());
        }

        return response;
    }

    /**
     * 手动更新指定居所的事件
     *
     * @param residence   居所类型
     * @param requestBody 请求体
     * @return 更新结果
     */
    @PostMapping("/{residence}/update")
    public Map<String, Object> updateResidenceEvent(@PathVariable String residence, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 验证居所类型
            if (ResidenceUtils.isInvalidResidence(residence)) {
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
     * 使用Gson解析事件数据JSON
     *
     * @param eventDataJson JSON格式的事件数据
     * @return 解析后的事件列表
     */
    private List<EventItem> parseEventDataWithGson(String eventDataJson) {
        if (eventDataJson == null || eventDataJson.trim().isEmpty() || eventDataJson.equals("[]")) {
            // 返回默认事件
            List<EventItem> defaultEvents = new ArrayList<>();
            defaultEvents.add(new EventItem("暂无事件", "normal"));
            return defaultEvents;
        }

        try {
            // 使用Gson反序列化
            List<EventItem> events = gson.fromJson(eventDataJson, eventListType);

            if (events == null || events.isEmpty()) {
                // 如果解析结果为空，返回默认事件
                List<EventItem> defaultEvents = new ArrayList<>();
                defaultEvents.add(new EventItem("事件解析为空", "normal"));
                return defaultEvents;
            }

            // 确保每个事件都有正确的type和colors
            for (EventItem event : events) {
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
            List<EventItem> errorEvents = new ArrayList<>();
            errorEvents.add(new EventItem("事件数据格式错误", "normal",
                    new String[]{"#ff6b6b", "#feca57"}));
            return errorEvents;
        }
    }
}
