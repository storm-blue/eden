package com.eden.lottery.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 奖品初始化服务
 * 注意：现在奖品配置已改为代码中的静态配置，不再需要数据库初始化
 */
@Service
@Order(2) // 确保在数据库初始化之后执行
public class PrizeInitService implements ApplicationRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(PrizeInitService.class);
    
    @Override
    public void run(ApplicationArguments args) {
        logger.info("奖品配置已改为代码静态配置，无需数据库初始化");
        logger.info("当前奖品配置请查看 LotteryService.STATIC_PRIZES");
    }
}
