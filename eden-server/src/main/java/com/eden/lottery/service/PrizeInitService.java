package com.eden.lottery.service;

import com.eden.lottery.entity.Prize;
import com.eden.lottery.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 奖品初始化服务
 */
@Service
@Order(2) // 确保在数据库初始化之后执行
public class PrizeInitService implements ApplicationRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(PrizeInitService.class);
    
    @Autowired
    private PrizeMapper prizeMapper;
    
    @Override
    public void run(ApplicationArguments args) {
        try {
            initializePrizes();
        } catch (Exception e) {
            logger.error("初始化奖品失败", e);
        }
    }
    
    /**
     * 初始化默认奖品
     */
    private void initializePrizes() {
        List<Prize> existingPrizes = prizeMapper.selectAll();
        
        if (existingPrizes.isEmpty()) {
            logger.info("开始初始化奖品数据...");
            
            // 创建默认奖品
            Prize[] defaultPrizes = {
                new Prize("🍰 吃的～", 0.15, "common"),
                new Prize("🥤 喝的～", 0.20, "common"),
                new Prize("❤️ 爱", 0.01, "epic"),
                new Prize("💸 空空如也", 0.25, "common"),
                new Prize("🧧 红包", 0.10, "uncommon"),
                new Prize("🔄 再转一次", 0.25, "special"),
                new Prize("🎁 随机礼物", 0.04, "rare")
            };
            
            for (Prize prize : defaultPrizes) {
                prizeMapper.insert(prize);
                logger.info("初始化奖品: {} - 概率: {}%", prize.getName(), prize.getProbability() * 100);
            }
            
            logger.info("奖品初始化完成，共创建 {} 个奖品", defaultPrizes.length);
        } else {
            logger.info("奖品已存在，跳过初始化。当前奖品数量: {}", existingPrizes.size());
        }
    }
}
