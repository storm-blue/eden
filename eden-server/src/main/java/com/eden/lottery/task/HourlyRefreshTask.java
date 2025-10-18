package com.eden.lottery.task;

import com.eden.lottery.service.SpecialResidenceService;
import com.eden.lottery.service.StarCityService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 每小时刷新定时任务
 * 处理特殊居住组合的人口增长加成
 */
@Component
public class HourlyRefreshTask {
    
    private static final Logger logger = LoggerFactory.getLogger(HourlyRefreshTask.class);
    
    @Resource
    private SpecialResidenceService specialResidenceService;
    
    @Resource
    private StarCityService starCityService;
    
    /**
     * 每小时执行特殊居住组合人口增长和饥饿检查
     * cron表达式: 秒 分 时 日 月 周
     * "0 0 * * * ?" 表示每小时的0分0秒执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void hourlyPopulationBonus() {
        logger.info("开始执行每小时特殊居住组合人口增长和饥饿检查...");
        
        try {
            // 1. 检查并处理人口饥饿
            starCityService.checkAndHandleStarvation();
            
            // 2. 计算当前特殊居住组合的人口增长加成
            int hourlyBonus = specialResidenceService.calculateHourlyPopulationBonus();
            
            if (hourlyBonus > 0) {
                // 应用人口增长加成
                starCityService.applyHourlyPopulationBonus(hourlyBonus);
                logger.info("特殊居住组合人口增长完成：+{} 人口", hourlyBonus);
            } else {
                logger.info("当前没有特殊居住组合，跳过人口增长加成");
            }
            
        } catch (Exception e) {
            logger.error("每小时特殊居住组合人口增长和饥饿检查失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试方法：每隔2分钟执行一次（仅用于开发测试）
     * 生产环境中可以注释掉这个方法
     */
    // @Scheduled(fixedRate = 120000) // 2分钟 = 120,000毫秒
    public void testHourlyTasks() {
        logger.info("[测试] 执行每小时任务测试...");
        
        try {
            // 1. 测试饥饿检查
            starCityService.checkAndHandleStarvation();
            
            // 2. 测试人口增长
            int hourlyBonus = specialResidenceService.calculateHourlyPopulationBonus();
            if (hourlyBonus > 0) {
                starCityService.applyHourlyPopulationBonus(hourlyBonus);
                logger.info("[测试] 特殊居住组合人口增长测试完成：+{} 人口", hourlyBonus);
            } else {
                logger.info("[测试] 当前没有特殊居住组合");
            }
        } catch (Exception e) {
            logger.error("[测试] 每小时任务测试失败: {}", e.getMessage(), e);
        }
    }
}
