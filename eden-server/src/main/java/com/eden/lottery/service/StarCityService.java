package com.eden.lottery.service;

import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.mapper.LotteryRecordMapper;
import com.eden.lottery.mapper.StarCityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 星星城服务层
 */
@Service
public class StarCityService {

    private static final Logger logger = LoggerFactory.getLogger(StarCityService.class);

    @Autowired
    private StarCityMapper starCityMapper;

    @Autowired
    private LotteryRecordMapper lotteryRecordMapper;

    /**
     * 获取星星城数据
     */
    public StarCity getStarCity() {
        StarCity starCity = starCityMapper.getStarCity();
        if (starCity == null) {
            // 如果没有数据，初始化默认数据
            starCity = initializeStarCity();
        }
        return starCity;
    }

    /**
     * 初始化星星城数据
     */
    @Transactional
    public StarCity initializeStarCity() {
        StarCity starCity = new StarCity(100000L, 100000L, 20);
        starCityMapper.insertStarCity(starCity);
        return starCityMapper.getStarCity();
    }

    /**
     * 更新星星城数据
     */
    @Transactional
    public StarCity updateStarCity(StarCity starCity) {
        starCity.setLevel(starCity.calculateLevel());
        starCity.setUpdateTime(LocalDateTime.now());
        starCityMapper.updateStarCity(starCity);
        return starCityMapper.getStarCity();
    }

    /**
     * 每日更新星星城数据
     * 人口+10000，食物+5000，幸福指数-1
     */
    @Transactional
    public void dailyUpdate() {
        // 先确保有数据
        StarCity current = getStarCity();
        
        // 执行每日更新
        starCityMapper.dailyUpdate();
        
        // 重新计算等级
        StarCity updated = starCityMapper.getStarCity();
        if (updated != null) {
            updated.setLevel(updated.calculateLevel());
            updated.setUpdateTime(LocalDateTime.now());
            starCityMapper.updateStarCity(updated);
        }
    }

    /**
     * 应用每小时人口增长加成（特殊居住组合）
     * @param bonusPopulation 增长的人口数量
     */
    @Transactional
    public void applyHourlyPopulationBonus(int bonusPopulation) {
        if (bonusPopulation <= 0) {
            return;
        }
        
        // 先确保有数据
        StarCity current = getStarCity();
        
        // 应用人口增长加成
        starCityMapper.addPopulation(bonusPopulation);
        
        // 重新计算等级
        StarCity updated = starCityMapper.getStarCity();
        if (updated != null) {
            updated.setLevel(updated.calculateLevel());
            updated.setUpdateTime(LocalDateTime.now());
            starCityMapper.updateStarCity(updated);
            
            logger.info("应用特殊居住组合人口加成：+{} 人口，当前人口：{}", 
                       bonusPopulation, updated.getPopulation());
        }
    }

    /**
     * 获取等级信息
     */
    public String getLevelInfo(int level) {
        switch (level) {
            case 1:
                return "新兴小镇 - 人口10万，食物10万，幸福指数10";
            case 2:
                return "繁荣城镇 - 人口20万，食物20万，幸福指数30";
            case 3:
                return "现代都市 - 人口40万，食物40万，幸福指数50";
            case 4:
                return "超级城市 - 人口70万，食物70万，幸福指数80";
            case 5:
                return "梦幻星城 - 人口100万，食物100万，幸福指数100";
            default:
                return "未知等级";
        }
    }

    /**
     * 检查是否需要升级
     */
    public boolean canUpgrade(StarCity starCity) {
        int currentLevel = starCity.getLevel();
        int calculatedLevel = starCity.calculateLevel();
        return calculatedLevel > currentLevel;
    }

    /**
     * 格式化数字显示
     */
    public String formatNumber(Long number) {
        if (number >= 10000) {
            return String.format("%.1f万", number / 10000.0);
        }
        return number.toString();
    }

    /**
     * 处理用户捐献
     * @param userId 用户ID
     * @param prizeType 奖品类型
     * @return 捐献结果
     */
    @Transactional
    public boolean processDonation(String userId, String prizeType) {
        try {
            // 1. 检查用户是否有该奖品
            if (!userHasPrize(userId, prizeType)) {
                return false;
            }

            // 2. 获取当前星星城数据
            StarCity starCity = getStarCity();
            
            // 3. 根据奖品类型增加对应数值
            switch (prizeType) {
                case "🍰 吃的～":
                    starCity.setFood(starCity.getFood() + 10000L);
                    break;
                case "🥤 喝的～":
                    starCity.setFood(starCity.getFood() + 5000L);
                    starCity.setHappiness(starCity.getHappiness() + 1);
                    break;
                case "🎁 随机礼物":
                    starCity.setHappiness(starCity.getHappiness() + 2);
                    break;
                default:
                    return false;
            }
            
            // 4. 更新星星城数据
            starCity.setLevel(starCity.calculateLevel());
            starCity.setUpdateTime(LocalDateTime.now());
            starCityMapper.updateStarCity(starCity);
            
            // 5. 删除用户的奖品记录
            removeUserPrize(userId, prizeType);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查用户是否拥有指定奖品
     */
    private boolean userHasPrize(String userId, String prizeType) {
        int count = lotteryRecordMapper.countUserPrizeByName(userId, prizeType);
        return count > 0;
    }

    /**
     * 移除用户的奖品记录
     */
    private void removeUserPrize(String userId, String prizeType) {
        lotteryRecordMapper.deleteUserPrizeByName(userId, prizeType);
    }

    /**
     * 获取用户可捐献的奖品列表
     */
    public List<Map<String, Object>> getUserDonationPrizes(String userId) {
        logger.info("获取用户可捐献奖品列表，用户ID: {}", userId);
        
        // 获取用户的抽奖记录
        List<LotteryRecord> records = lotteryRecordMapper.selectByUserId(userId, 0, 1000);
        logger.info("查询到用户 {} 的抽奖记录数量: {}", userId, records.size());
        
        // 统计可捐献的奖品
        Map<String, Integer> prizeCount = new HashMap<>();
        for (LotteryRecord record : records) {
            if (record.getPrize() != null) {
                String prizeName = record.getPrize().getName();
                logger.debug("处理奖品记录: {}", prizeName);
                // 只统计可捐献的奖品
                if ("🍰 吃的～".equals(prizeName) || "🥤 喝的～".equals(prizeName) || "🎁 随机礼物".equals(prizeName)) {
                    prizeCount.put(prizeName, prizeCount.getOrDefault(prizeName, 0) + 1);
                    logger.debug("找到可捐献奖品: {} (当前数量: {})", prizeName, prizeCount.get(prizeName));
                }
            } else {
                logger.warn("发现空奖品记录，记录ID: {}", record.getId());
            }
        }
        
        logger.info("统计结果: {}", prizeCount);
        
        // 转换为返回格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : prizeCount.entrySet()) {
            if (entry.getValue() > 0) {
                Map<String, Object> prizeInfo = new HashMap<>();
                prizeInfo.put("name", entry.getKey());
                prizeInfo.put("count", entry.getValue());
                prizeInfo.put("level", "common"); // 默认等级
                result.add(prizeInfo);
            }
        }
        
        // 按名称排序
        result.sort((a, b) -> ((String) a.get("name")).compareTo((String) b.get("name")));
        
        logger.info("返回可捐献奖品列表，数量: {}", result.size());
        return result;
    }
}
