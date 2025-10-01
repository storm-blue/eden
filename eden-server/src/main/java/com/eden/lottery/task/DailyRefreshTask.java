package com.eden.lottery.task;

import com.eden.lottery.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每日刷新抽奖次数定时任务
 */
@Component
public class DailyRefreshTask {
    
    private static final Logger logger = LoggerFactory.getLogger(DailyRefreshTask.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 每天凌晨0点刷新所有用户的抽奖次数
     * cron表达式: 秒 分 时 日 月 周
     * "0 0 0 * * ?" 表示每天0点0分0秒执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void refreshDailyDraws() {
        logger.info("开始执行每日抽奖次数刷新任务...");
        
        try {
            userService.batchRefreshDailyDraws();
            logger.info("每日抽奖次数刷新任务执行完成");
        } catch (Exception e) {
            logger.error("每日抽奖次数刷新任务执行失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试方法：每隔5分钟执行一次（仅用于开发测试）
     * 生产环境中可以注释掉这个方法
     */
    // @Scheduled(fixedRate = 300000) // 5分钟 = 300,000毫秒
    public void testRefresh() {
        logger.info("[测试] 执行抽奖次数刷新测试...");
        
        try {
            userService.batchRefreshDailyDraws();
            logger.info("[测试] 抽奖次数刷新测试完成");
        } catch (Exception e) {
            logger.error("[测试] 抽奖次数刷新测试失败: {}", e.getMessage(), e);
        }
    }
}
