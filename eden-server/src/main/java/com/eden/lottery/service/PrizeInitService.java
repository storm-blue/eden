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
     * 每次启动都会清空现有数据并重新插入默认奖品配置
     */
    @Transactional
    private void reinitializePrizes() {
        logger.info("开始重新初始化奖品数据...");

        // 查询现有奖品数量
        List<Prize> existingPrizes = prizeMapper.selectAll();
        if (!existingPrizes.isEmpty()) {
            logger.info("发现现有奖品 {} 个，正在清理...", existingPrizes.size());
            int deletedCount = prizeMapper.deleteAll();
            logger.info("✅ 成功删除 {} 个现有奖品", deletedCount);
        } else {
            logger.info("数据库中无现有奖品数据");
        }

        // 创建默认奖品配置
        // 注意：顺序必须与前端LuckyWheel.jsx中的prizes数组顺序一致！
        Prize[] defaultPrizes = {
                new Prize("🍰 吃的～", 0.08, "common"),
                new Prize("🥤 喝的～", 0.08, "common"),
                new Prize("❤️ 爱", 0.002, "epic"),
                new Prize("💸 空空如也", 0.40, "common"),
                new Prize("🧧 红包", 0.05, "uncommon"),
                new Prize("🔄 再转一次", 0.30, "special"),
                new Prize("🎁 随机礼物", 0.028, "rare"),
                new Prize("💬 陪聊服务", 0.06, "rare")
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

        // 插入新的奖品数据
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            prizeMapper.insert(prize);
            logger.info("插入奖品[{}]: {} - 概率: {}% - 级别: {}",
                    i, prize.getName(), prize.getProbability() * 100, prize.getLevel());
        }

        logger.info("🎉 奖品重新初始化完成！共配置 {} 个奖品，概率总和: {}%",
                defaultPrizes.length, totalProbability * 100);

        // 输出配置摘要
        logger.info("📋 奖品配置摘要:");
        for (int i = 0; i < defaultPrizes.length; i++) {
            Prize prize = defaultPrizes[i];
            logger.info("   索引{}: {} ({}%)", i, prize.getName(), prize.getProbability() * 100);
        }
    }
}
