package com.eden.lottery.task;

import com.eden.lottery.service.GiantAttackService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 巨人进攻定时任务
 */
@Component
public class GiantAttackTask {
    
    private static final Logger logger = LoggerFactory.getLogger(GiantAttackTask.class);
    
    @Resource
    private GiantAttackService giantAttackService;
    
    /**
     * 每6小时检查一次巨人进攻
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 0 */6 * * ?")
    public void checkGiantAttack() {
        logger.info("开始执行巨人进攻检查...");
        
        try {
            giantAttackService.checkGiantAttack();
        } catch (Exception e) {
            logger.error("巨人进攻检查失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 每10分钟处理一次巨人伤害
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void processGiantDamage() {
        try {
            giantAttackService.processGiantDamage();
        } catch (Exception e) {
            logger.error("处理巨人伤害失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试方法：每隔1分钟执行一次巨人进攻检查（仅用于开发测试）
     * 生产环境中可以注释掉这个方法
     */
    // @Scheduled(fixedRate = 60000) // 1分钟 = 60,000毫秒
    public void testGiantAttackCheck() {
        logger.info("[测试] 执行巨人进攻检查测试...");
        
        try {
            giantAttackService.checkGiantAttack();
        } catch (Exception e) {
            logger.error("[测试] 巨人进攻检查测试失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试方法：每隔30秒执行一次巨人伤害处理（仅用于开发测试）
     * 生产环境中可以注释掉这个方法
     */
    // @Scheduled(fixedRate = 30000) // 30秒 = 30,000毫秒
    public void testGiantDamage() {
        logger.info("[测试] 执行巨人伤害处理测试...");
        
        try {
            giantAttackService.processGiantDamage();
        } catch (Exception e) {
            logger.error("[测试] 巨人伤害处理测试失败: {}", e.getMessage(), e);
        }
    }
}
