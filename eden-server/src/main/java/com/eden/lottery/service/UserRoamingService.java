package com.eden.lottery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户漫游系统服务
 * 每隔2小时自动触发，让所有用户可能移动到新的居所
 */
@Service
public class UserRoamingService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoamingService.class);

    @Autowired
    private StarCityService starCityService;

    @Autowired
    private UserRoamingLogicService roamingLogicService; // 用户自定义漫游逻辑

    @Autowired
    private ResidenceEventService residenceEventService; // 居所事件服务

    /**
     * 定时任务：每半小时执行一次用户漫游
     */
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void executeUserRoaming() {
        logger.info("开始执行用户漫游系统...");

        try {
            // 获取所有用户的当前居住信息
            Map<String, List<String>> allResidents = starCityService.getAllBuildingResidents();

            int totalUsers = 0;
            int movedUsers = 0;

            // 遍历所有建筑和用户
            for (Map.Entry<String, List<String>> entry : allResidents.entrySet()) {
                String currentBuilding = entry.getKey();
                List<String> residents = entry.getValue();

                for (String username : residents) {
                    totalUsers++;

                    try {
                        // 调用用户自定义的漫游逻辑
                        String newBuilding = roamingLogicService.determineNewResidence(username, currentBuilding);

                        // 如果返回新居所且与当前不同，则执行移动
                        if (newBuilding != null && !newBuilding.equals(currentBuilding)) {
                            boolean moveSuccess = starCityService.moveUserToBuilding(username, currentBuilding, newBuilding);

                            if (moveSuccess) {
                                movedUsers++;
                                logger.info("用户 {} 从 {} 漫游到 {}", username, currentBuilding, newBuilding);
                                
                                // 🔥 新增：为离开和入住的居所生成相应事件
                                generateMoveEvents(username, currentBuilding, newBuilding);
                            } else {
                                logger.warn("用户 {} 从 {} 移动到 {} 失败", username, currentBuilding, newBuilding);
                            }
                        }

                    } catch (Exception e) {
                        logger.error("处理用户 {} 的漫游时发生错误: {}", username, e.getMessage(), e);
                    }
                }
            }

            logger.info("用户漫游系统执行完成 - 总用户数: {}, 移动用户数: {}", totalUsers, movedUsers);

            // 无论如何都刷新所有居所事件，一个定时任务做两件事：用户漫游，居所事件刷新。
            try {
                int refreshedCount = residenceEventService.refreshAllResidenceEvents();
                logger.info("居所事件刷新完成，成功刷新 {} 个居所", refreshedCount);
            } catch (Exception e) {
                logger.error("刷新居所事件时发生错误: {}", e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error("执行用户漫游系统时发生错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 手动触发用户漫游（用于测试）
     */
    public void manualTriggerRoaming() {
        logger.info("手动触发用户漫游系统");
        executeUserRoaming();
    }

    /**
     * 获取漫游统计信息
     */
    public Map<String, Object> getRoamingStats() {
        // 可以添加统计信息，如最后执行时间、移动次数等
        return Map.of(
                "lastExecutionTime", "待实现",
                "totalRoamingCount", "待实现",
                "systemStatus", "运行中"
        );
    }

    /**
     * 为用户移动生成居所事件
     * 为离开的居所生成"xxx离开了"事件，为入住的居所生成"xxx入住了"事件
     * 
     * @param username 移动的用户名
     * @param fromResidence 离开的居所
     * @param toResidence 入住的居所
     */
    private void generateMoveEvents(String username, String fromResidence, String toResidence) {
        try {
            // 为离开的居所生成事件
            generateDepartureEvent(username, fromResidence);
            
            // 为入住的居所生成事件
            generateArrivalEvent(username, toResidence);
            
            logger.info("已为用户 {} 的移动生成居所事件：{} -> {}", username, 
                    getResidenceDisplayName(fromResidence), 
                    getResidenceDisplayName(toResidence));
                    
        } catch (Exception e) {
            logger.error("生成用户 {} 移动事件时发生错误: {}", username, e.getMessage(), e);
        }
    }

    /**
     * 生成离开事件
     */
    private void generateDepartureEvent(String username, String residence) {
        try {
            // 创建离开事件
            List<com.eden.lottery.dto.ResidenceEventItem> events = new ArrayList<>();
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                username + " 离开了" + getResidenceDisplayName(residence), "normal"));
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                getResidenceDisplayName(residence) + "变得安静了...", "normal"));
            
            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);
            
            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);
            
            logger.debug("生成离开事件：{} 离开了 {}", username, getResidenceDisplayName(residence));
            
        } catch (Exception e) {
            logger.error("生成离开事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }

    /**
     * 生成入住事件
     */
    private void generateArrivalEvent(String username, String residence) {
        try {
            // 创建入住事件
            List<com.eden.lottery.dto.ResidenceEventItem> events = new ArrayList<>();
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                username + " 入住了" + getResidenceDisplayName(residence), "normal"));
            events.add(new com.eden.lottery.dto.ResidenceEventItem(
                getResidenceDisplayName(residence) + "迎来了新的住客", "normal"));
            
            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);
            
            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);
            
            logger.debug("生成入住事件：{} 入住了 {}", username, getResidenceDisplayName(residence));
            
        } catch (Exception e) {
            logger.error("生成入住事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }

    /**
     * 获取居所的显示名称
     */
    private String getResidenceDisplayName(String residence) {
        switch (residence) {
            case "castle":
                return "城堡🏰";
            case "park":
                return "公园🌳";
            case "city_hall":
                return "市政厅🏛️";
            case "white_dove_house":
                return "小白鸽家🕊️";
            case "palace":
                return "行宫🏯";
            default:
                return residence;
        }
    }
}
