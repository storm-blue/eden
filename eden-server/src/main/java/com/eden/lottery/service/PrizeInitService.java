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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 奖品初始化服务
 * 每次应用启动时都会重新初始化奖品数据
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
            reinitializePrizes();
        } catch (Exception e) {
            logger.error("重新初始化奖品失败", e);
        }
    }

    /**
     * 重新初始化奖品数据
     * 使用UPSERT模式：存在则更新，不存在则插入
     */
    @Transactional
    private void reinitializePrizes() {
        logger.info("开始重新初始化奖品数据...");

        // 查询现有奖品数量
        List<Prize> existingPrizes = prizeMapper.selectAll();
        logger.info("当前数据库中有 {} 个奖品", existingPrizes.size());

        // 创建默认奖品配置（带固定ID）
        // 注意：顺序必须与前端LuckyWheel.jsx中的prizes数组顺序一致！
        Prize[] defaultPrizes = {
            createPrizeWithId(1L, "🍰 吃的～", 0.08, "common"),      // 索引0: 8%
            createPrizeWithId(2L, "🥤 喝的～", 0.08, "common"),      // 索引1: 8%
            createPrizeWithId(3L, "❤️ 爱", 0.002, "epic"),           // 索引2: 0.2%
            createPrizeWithId(4L, "💸 空空如也", 0.40, "common"),    // 索引3: 40%
            createPrizeWithId(5L, "🧧 红包", 0.05, "uncommon"),      // 索引4: 5%
            createPrizeWithId(6L, "🔄 再转一次", 0.30, "special"),   // 索引5: 30%
            createPrizeWithId(7L, "🎁 随机礼物", 0.028, "rare"),     // 索引6: 2.8%
            createPrizeWithId(8L, "💬 陪聊服务", 0.06, "rare")       // 索引7: 6%
        };

        // 验证概率总和
        double totalProbability = 0.0;
        for (Prize prize : defaultPrizes) {
            totalProbability += prize.getProbability();
        }

        if (Math.abs(totalProbability - 1.0) > 0.001) {
            logger.warn("⚠️ 警告：奖品概率总和为 {}, 不等于1.0，可能导致抽奖异常", totalProbability);
        } else {
            logger.info("✅ 概率验证通过：总和为 {}", totalProbability);
        }

        // 使用UPSERT插入或更新奖品数据
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            int result = prizeMapper.insertOrUpdate(prize);
            String action = existingPrizes.stream().anyMatch(p -> p.getId().equals(prize.getId())) ? "更新" : "插入";
            logger.info("{}奖品[{}]: {} - 概率: {}% - 级别: {}", 
                       action, i, prize.getName(), prize.getProbability() * 100, prize.getLevel());
        }

        logger.info("🎉 奖品重新初始化完成！共配置 {} 个奖品，概率总和: {}%",
                defaultPrizes.length, totalProbability * 100);

        // 输出配置摘要
        logger.info("📋 奖品配置摘要:");
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            logger.info("   ID{}/索引{}: {} ({}%)", prize.getId(), i, prize.getName(), prize.getProbability() * 100);
        }
    }

    /**
     * 创建带ID的奖品对象
     */
    private Prize createPrizeWithId(Long id, String name, Double probability, String level) {
        Prize prize = new Prize(name, probability, level);
        prize.setId(id);
        return prize;
    }
}
