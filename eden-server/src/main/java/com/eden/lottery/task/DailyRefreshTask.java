package com.eden.lottery.task;

import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.service.MagicService;
import com.eden.lottery.service.StarCityService;
import com.eden.lottery.service.UserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每日刷新定时任务
 */
@Component
public class DailyRefreshTask {
    
    private static final Logger logger = LoggerFactory.getLogger(DailyRefreshTask.class);
    
    @Resource
    private UserService userService;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private StarCityService starCityService;
    
    @Resource
    private MagicService magicService;
    
    /**
     * 每天凌晨0点执行每日任务
     * 1. 刷新所有用户的抽奖次数
     * 2. 更新星星城数据
     * 3. 刷新用户精力值
     * cron表达式: 秒 分 时 日 月 周
     * "0 0 0 * * ?" 表示每天0点0分0秒执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyTasks() {
        logger.info("开始执行每日任务...");
        
        try {
            // 1. 刷新用户抽奖次数
            userService.batchRefreshDailyDraws();
            logger.info("用户抽奖次数刷新完成");
            
            // 2. 更新星星城数据 (人口增长为当前食物的1/8，食物+5000，幸福-1)
            starCityService.dailyUpdate();
            logger.info("星星城数据更新完成");
            
            // 3. 刷新用户精力值到满值
            userMapper.batchRefreshEnergy();
            logger.info("用户精力值刷新完成");
            
            logger.info("每日任务执行完成");
        } catch (Exception e) {
            logger.error("每日任务执行失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试方法：每隔5分钟执行一次（仅用于开发测试）
     * 生产环境中可以注释掉这个方法
     */
    // @Scheduled(fixedRate = 300000) // 5分钟 = 300,000毫秒
    public void testTasks() {
        logger.info("[测试] 执行每日任务测试...");
        
        try {
            userService.batchRefreshDailyDraws();
            starCityService.dailyUpdate();
            userMapper.batchRefreshEnergy();
            logger.info("[测试] 每日任务测试完成");
        } catch (Exception e) {
            logger.error("[测试] 每日任务测试失败: {}", e.getMessage(), e);
        }
    }
}
