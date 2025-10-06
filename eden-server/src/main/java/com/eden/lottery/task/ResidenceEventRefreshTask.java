package com.eden.lottery.task;

import com.eden.lottery.service.ResidenceEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 居所事件定时刷新任务
 */
@Component
public class ResidenceEventRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceEventRefreshTask.class);

    @Autowired
    private ResidenceEventService residenceEventService;

    /**
     * 每小时刷新居所事件
     * 停用，交给 com.eden.lottery.service.UserRoamingService#executeUserRoaming() 触发
     */
    // @Scheduled(initialDelay = 360, fixedDelay = 3600, timeUnit = TimeUnit.SECONDS)
    public void refreshResidenceEvents() {
        logger.info("开始执行居所事件定时刷新任务");

        try {
            int successCount = residenceEventService.refreshAllResidenceEvents();
            logger.info("居所事件定时刷新完成，成功刷新 {}/5 个居所", successCount);
        } catch (Exception e) {
            logger.error("居所事件定时刷新任务执行失败", e);
        }
    }

    /**
     * 手动触发居所事件刷新（用于测试）
     */
    public void manualRefresh() {
        logger.info("手动触发居所事件刷新");
        refreshResidenceEvents();
    }
}
