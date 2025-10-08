package com.eden.lottery.service;

import com.eden.lottery.entity.LotteryRecord;
import com.eden.lottery.entity.StarCity;
import com.eden.lottery.entity.User;
import com.eden.lottery.event.ResidenceEventItem;
import com.eden.lottery.mapper.LotteryRecordMapper;
import com.eden.lottery.mapper.StarCityMapper;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.utils.ResidenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResidenceEventService residenceEventService;

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
     *
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
        return switch (level) {
            case 1 -> "新兴小镇 - 人口10万，食物10万，幸福指数10";
            case 2 -> "繁荣城镇 - 人口20万，食物20万，幸福指数30";
            case 3 -> "现代都市 - 人口40万，食物40万，幸福指数50";
            case 4 -> "超级城市 - 人口70万，食物70万，幸福指数80";
            case 5 -> "梦幻星城 - 人口100万，食物100万，幸福指数100";
            default -> "未知等级";
        };
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
     *
     * @param userId    用户ID
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

    // ==================== 用户漫游系统相关方法 ====================

    /**
     * 获取所有建筑的居住人员
     *
     * @return 建筑名称 -> 居住人员列表的映射
     */
    public Map<String, List<String>> getAllBuildingResidents() {
        logger.info("获取所有建筑的居住人员");

        Map<String, List<String>> result = new HashMap<>();
        String[] buildings = ResidenceUtils.getAllResidences();

        for (String building : buildings) {
            try {
                List<User> residents = userMapper.selectByResidence(building);
                List<String> usernames = residents.stream()
                        .map(User::getUserId)
                        .collect(Collectors.toList());
                result.put(building, usernames);

                logger.debug("建筑 {} 的居住人员: {}", building, usernames);
            } catch (Exception e) {
                logger.error("获取建筑 {} 的居住人员失败: {}", building, e.getMessage(), e);
                result.put(building, new ArrayList<>());
            }
        }

        return result;
    }

    /**
     * 移动用户到新建筑（完整版本，包含事件生成）
     * 这是统一的用户移动方法，支持自动漫游和手动移动
     *
     * @param username     用户名
     * @param fromBuilding 原建筑
     * @param toBuilding   目标建筑
     * @param moveReason   移动原因（"roaming" 或 "manual"）
     * @return 移动是否成功
     */
    @Transactional
    public boolean moveUserToBuilding(String username, String fromBuilding, String toBuilding, String moveReason) {
        logger.info("移动用户 {} 从 {} 到 {} (原因: {})", username, fromBuilding, toBuilding, moveReason);

        try {
            // 验证建筑名称
            if (ResidenceUtils.isInvalidResidence(toBuilding)) {
                logger.error("无效的目标建筑: {}", toBuilding);
                return false;
            }

            // 检查是否为相同建筑（避免无意义的移动）
            if (toBuilding.equals(fromBuilding)) {
                logger.debug("用户 {} 已在目标建筑 {} 中，无需移动", username, ResidenceUtils.getDisplayName(toBuilding));
                return false;
            }

            // 更新用户居住地
            userMapper.updateResidence(username, toBuilding);

            // 生成移动事件
            generateMoveEvents(username, fromBuilding, toBuilding);

            logger.info("用户 {} 成功从 {} 移动到 {} ({})",
                    username, ResidenceUtils.getDisplayName(fromBuilding),
                    ResidenceUtils.getDisplayName(toBuilding), moveReason);
            return true;

        } catch (Exception e) {
            logger.error("移动用户 {} 从 {} 到 {} 失败: {}", username, fromBuilding, toBuilding, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 为用户移动生成居所事件
     * 为离开的居所生成"xxx离开了"事件，为入住的居所生成"xxx入住了"事件
     *
     * @param username      移动的用户名
     * @param fromResidence 离开的居所
     * @param toResidence   入住的居所
     */
    private void generateMoveEvents(String username, String fromResidence, String toResidence) {
        try {
            // 为离开的居所生成事件
            generateDepartureEvent(username, fromResidence);

            // 为入住的居所生成事件
            generateArrivalEvent(username, toResidence);

            logger.info("已为用户 {} 的移动生成居所事件：{} -> {}", username,
                    ResidenceUtils.getDisplayName(fromResidence),
                    ResidenceUtils.getDisplayName(toResidence));

        } catch (Exception e) {
            logger.error("生成用户 {} 移动事件时发生错误: {}", username, e.getMessage(), e);
        }
    }

    /**
     * 生成离开事件
     */
    private void generateDepartureEvent(String username, String residence) {
        try {
            // 创建离开事件
            List<ResidenceEventItem> events = new ArrayList<>();
            events.add(new ResidenceEventItem(
                    username + " 离开了" + ResidenceUtils.getDisplayName(residence), "normal"));
            events.add(new ResidenceEventItem(
                    ResidenceUtils.getDisplayName(residence) + "变得安静了...", "normal"));

            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);

            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);

            logger.debug("生成离开事件：{} 离开了 {}", username, ResidenceUtils.getDisplayName(residence));

        } catch (Exception e) {
            logger.error("生成离开事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }

    /**
     * 生成入住事件
     */
    private void generateArrivalEvent(String username, String residence) {
        try {
            // 创建入住事件
            List<ResidenceEventItem> events = new ArrayList<>();
            events.add(new ResidenceEventItem(
                    username + " 入住了" + ResidenceUtils.getDisplayName(residence), "normal"));
            events.add(new ResidenceEventItem(
                    ResidenceUtils.getDisplayName(residence) + "迎来了新的住客", "normal"));

            // 序列化为JSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String eventData = gson.toJson(events);

            // 更新居所事件
            residenceEventService.updateResidenceEvent(residence, eventData, false, null, false);

            logger.debug("生成入住事件：{} 入住了 {}", username, ResidenceUtils.getDisplayName(residence));

        } catch (Exception e) {
            logger.error("生成入住事件失败，用户: {}, 居所: {}", username, residence, e);
        }
    }
}
