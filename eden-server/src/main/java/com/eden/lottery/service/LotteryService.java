package com.eden.lottery.service;

import com.eden.lottery.dto.LotteryResult;
import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.Prize;
import com.eden.lottery.mapper.LotteryRecordMapper;
import com.eden.lottery.mapper.PrizeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
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
    
    /**
     * 执行抽奖
     */
    @Transactional(timeout = 10)
    public LotteryResult drawLottery(String userId, String ipAddress, String userAgent) {
        try {
            // 获取所有有效奖品
            List<Prize> prizes = prizeMapper.selectValidPrizes();
            if (prizes.isEmpty()) {
                throw new RuntimeException("暂无可用奖品");
            }
            
            // 基于概率选择奖品
            Prize selectedPrize = selectPrizeByProbability(prizes);
            
            // 记录抽奖结果
            LotteryRecord record = new LotteryRecord(userId, selectedPrize.getId(), ipAddress, userAgent);
            recordMapper.insert(record);
            
            logger.info("用户 {} 抽中了 {}", userId, selectedPrize.getName());
            
            return new LotteryResult(selectedPrize, record.getId(), record.getCreatedAt());
            
        } catch (Exception e) {
            logger.error("抽奖失败: {}", e.getMessage(), e);
            throw new RuntimeException("抽奖系统暂时不可用，请稍后再试");
        }
    }
    
    /**
     * 基于概率选择奖品
     */
    private Prize selectPrizeByProbability(List<Prize> prizes) {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        
        logger.debug("随机数: {}", randomValue);
        
        for (Prize prize : prizes) {
            cumulativeProbability += prize.getProbability();
            logger.debug("奖品: {}, 累计概率: {}", prize.getName(), cumulativeProbability);
            
            if (randomValue <= cumulativeProbability) {
                return prize;
            }
        }
        
        // 如果没有选中任何奖品，返回最后一个奖品（通常是概率最小的）
        return prizes.get(prizes.size() - 1);
    }
    
    /**
     * 获取所有奖品（不包含概率信息）
     */
    public List<Prize> getAllPrizes() {
        return prizeMapper.selectValidPrizes();
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
