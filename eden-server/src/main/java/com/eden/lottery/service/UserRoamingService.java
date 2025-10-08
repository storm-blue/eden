package com.eden.lottery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Scheduled(cron = "0 10,40 * * * ?")
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
                            boolean moveSuccess = starCityService.moveUserToBuilding(username, currentBuilding, newBuilding, "roaming");

                            if (moveSuccess) {
                                movedUsers++;
                                logger.info("用户 {} 从 {} 漫游到 {}", username, currentBuilding, newBuilding);
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
}
