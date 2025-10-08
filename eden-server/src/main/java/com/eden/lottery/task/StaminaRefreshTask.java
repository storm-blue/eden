package com.eden.lottery.task;

import com.eden.lottery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 耐力恢复定时任务
 * 每30分钟刷新一次所有用户的耐力值为5
 */
@Component
public class StaminaRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(StaminaRefreshTask.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 每30分钟刷新一次所有用户的耐力值
     * cron表达式：每小时的20分和50分执行
     */
    @Scheduled(cron = "0 20,50 * * * ?")
    public void refreshStamina() {
        try {
            logger.info("开始执行耐力恢复任务...");
            
            userMapper.batchRefreshStamina();
            
            logger.info("耐力恢复任务执行成功：所有用户耐力已恢复至5");
        } catch (Exception e) {
            logger.error("耐力恢复任务执行失败", e);
        }
    }
}

