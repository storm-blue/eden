package com.eden.lottery.task;

import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.service.UserStatusService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户状态刷新定时任务
 * 每30分钟更新一次所有用户的状态
 */
@Component
public class UserStatusRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(UserStatusRefreshTask.class);

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserStatusService userStatusService;

    /**
     * 每30分钟刷新一次所有用户的状态
     * cron表达式：每小时的0分和30分执行
     */
    @Scheduled(cron = "0 0,30 * * * ?")
    @Transactional
    public void refreshUserStatus() {
        try {
            logger.info("开始执行用户状态刷新任务...");
            
            // 获取所有用户
            List<User> allUsers = userMapper.selectAll();
            
            if (allUsers == null || allUsers.isEmpty()) {
                logger.info("没有需要刷新状态的用户");
                return;
            }
            
            int updatedCount = 0;
            
            // 遍历所有用户，更新状态
            for (User user : allUsers) {
                try {
                    String userId = user.getUserId();
                    String residence = user.getResidence();
                    
                    // 调用状态决定逻辑
                    String newStatus = userStatusService.determineUserStatus(userId, residence);
                    
                    // 如果返回的状态不为null，则更新用户状态
                    if (newStatus != null && !newStatus.equals(user.getStatus())) {
                        userMapper.updateStatus(userId, newStatus);
                        updatedCount++;
                        logger.debug("用户 {} 的状态已更新为: {}", userId, newStatus);
                    }
                    
                } catch (Exception e) {
                    logger.error("更新用户 {} 的状态失败", user.getUserId(), e);
                }
            }
            
            logger.info("用户状态刷新任务执行完成 - 共检查 {} 个用户，更新 {} 个用户状态", 
                    allUsers.size(), updatedCount);
            
        } catch (Exception e) {
            logger.error("用户状态刷新任务执行失败", e);
        }
    }
}

