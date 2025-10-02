package com.eden.lottery.service;

import com.eden.lottery.dto.LotteryResult;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.Prize;
import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.LotteryRecordMapper;
import com.eden.lottery.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 抽奖服务
 */
@Service
@Transactional
public class LotteryService {

    private static final Logger logger = LoggerFactory.getLogger(LotteryService.class);
    private final SecureRandom random = new SecureRandom();

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private LotteryRecordMapper recordMapper;

    @Autowired
    private UserService userService;

    /**
     * 静态奖品配置列表（包含概率）
     * 注意：概率总和应该为1.0（100%）
     * 顺序必须与前端LuckyWheel.jsx中的prizes数组顺序一致！
     */
    private static final List<Prize> STATIC_PRIZES = Arrays.asList(
            new Prize("🍰 吃的～", 0.08, "common"),
            new Prize("🥤 喝的～", 0.08, "common"),
            new Prize("❤️ 爱", 0.002, "epic"),
            new Prize("💸 空空如也", 0.40, "common"),
            new Prize("🧧 红包", 0.05, "uncommon"),
            new Prize("🔄 再转一次", 0.30, "special"),
            new Prize("🎁 随机礼物", 0.028, "rare"),
            new Prize("💬 陪聊服务", 0.06, "rare")
    );

    /**
     * 执行抽奖
     */
    @Transactional(timeout = 10)
    public LotteryResult drawLottery(String userId, String ipAddress, String userAgent) {
        try {
            // 检查用户是否存在
            User user = userService.getUserInfo(userId);
            if (user == null) {
                throw new RuntimeException("用户不存在，请联系管理员创建账户！");
            }

            // 检查用户是否可以抽奖
            if (!userService.canDraw(userId)) {
                throw new RuntimeException("您的抽奖次数已用完，请明天再来！");
            }

            // 扣减用户抽奖次数
            if (!userService.decreaseDraws(userId)) {
                throw new RuntimeException("抽奖次数扣减失败，请稍后再试");
            }

            // 基于概率选择奖品
            Prize selectedPrize = selectPrizeByProbability();

            // 记录抽奖结果
            // 由于现在使用静态奖品配置，我们将prizeId设为0（或者使用奖品在列表中的索引）
            Long prizeIndex = (long) STATIC_PRIZES.indexOf(selectedPrize);
            LotteryRecord record = new LotteryRecord(userId, prizeIndex, ipAddress, userAgent);
            record.setPrize(selectedPrize); // 设置奖品对象，用于显示
            recordMapper.insert(record);

            // 检查是否抽到"再转一次"，如果是则增加抽奖次数
            if ("🔄 再转一次".equals(selectedPrize.getName())) {
                boolean increased = userService.increaseRemainingDraws(userId, 1);
                if (increased) {
                    logger.info("用户 {} 抽中'再转一次'，剩余抽奖次数+1", userId);
                } else {
                    logger.warn("用户 {} 抽中'再转一次'，但次数增加失败", userId);
                }
            }

            // 获取用户剩余抽奖次数
            int remainingDraws = userService.getRemainingDraws(userId);

            logger.info("用户 {} 抽中了 {}, 剩余抽奖次数: {}", userId, selectedPrize.getName(), remainingDraws);

            return new LotteryResult(selectedPrize, record.getId(), record.getCreatedAt());

        } catch (Exception e) {
            logger.error("抽奖失败: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 基于静态概率配置选择奖品
     */
    private Prize selectPrizeByProbability() {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        logger.debug("随机数: {}", randomValue);

        for (Prize prize : STATIC_PRIZES) {
            cumulativeProbability += prize.getProbability();
            logger.debug("奖品: {}, 累计概率: {}", prize.getName(), cumulativeProbability);

            if (randomValue <= cumulativeProbability) {
                return prize;
            }
        }

        // 如果没有选中任何奖品，返回最后一个奖品（通常是概率最小的）
        return STATIC_PRIZES.get(STATIC_PRIZES.size() - 1);
    }

    /**
     * 获取所有奖品（不包含概率信息）
     */
    public List<Prize> getAllPrizes() {
        // 返回静态奖品列表的副本，但不包含概率信息
        return STATIC_PRIZES.stream()
                .map(prize -> {
                    Prize publicPrize = new Prize();
                    publicPrize.setName(prize.getName());
                    publicPrize.setLevel(prize.getLevel());
                    return publicPrize;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取用户抽奖记录
     */
    public List<LotteryRecord> getUserRecords(String userId, int limit) {
        return recordMapper.selectByUserId(userId, 0, limit);
    }

    /**
     * 获取最近抽奖记录
     */
    public List<LotteryRecord> getRecentRecords() {
        return recordMapper.selectRecentRecords(50);
    }

    /**
     * 获取抽奖统计信息
     */
    public Object getStatistics() {
        List<Map<String, Object>> prizesStatistics = recordMapper.selectPrizeStatistics();
        long drawsCount = recordMapper.count();

        return new Object() {
            public final long totalDraws = drawsCount;
            public final List<Map<String, Object>> prizeStats = prizesStatistics;
            public final LocalDateTime lastUpdate = LocalDateTime.now();
        };
    }
}
