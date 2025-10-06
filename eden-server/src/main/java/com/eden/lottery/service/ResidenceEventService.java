package com.eden.lottery.service;

import com.eden.lottery.dto.ResidenceEventItem;
import com.eden.lottery.entity.ResidenceEvent;
import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.ResidenceEventMapper;
import com.eden.lottery.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 居所事件服务
 */
@Service
public class ResidenceEventService {

    private static final Logger logger = LoggerFactory.getLogger(ResidenceEventService.class);

    @Autowired
    private ResidenceEventMapper residenceEventMapper;

    @Autowired
    private UserMapper userMapper;

    // Gson实例用于JSON序列化和反序列化
    private final Gson gson = new Gson();
    private final Type eventListType = new TypeToken<List<ResidenceEventItem>>() {
    }.getType();

    /**
     * 获取指定居所的当前事件
     *
     * @param residence 居所类型
     * @return 居所事件，如果没有则返回null
     */
    public ResidenceEvent getResidenceEvent(String residence) {
        try {
            return residenceEventMapper.selectByResidence(residence);
        } catch (Exception e) {
            logger.error("获取居所事件失败，居所: {}", residence, e);
            return null;
        }
    }

    /**
     * 更新居所事件（支持多条事件描述和特殊特效）
     *
     * @param residence         居所类型
     * @param eventData         事件数据（JSON格式）
     * @param showHeartEffect   是否显示爱心特效
     * @param specialText       特殊文字（如情侣组合文字）
     * @param showSpecialEffect 是否显示特殊特效（爱心飞舞和粉红色特效）
     * @return 是否更新成功
     */
    public boolean updateResidenceEvent(String residence, String eventData, Boolean showHeartEffect, String specialText, Boolean showSpecialEffect) {
        try {
            ResidenceEvent event = new ResidenceEvent(residence, eventData, showHeartEffect, specialText, showSpecialEffect);
            event.setUpdatedAt(LocalDateTime.now());

            int result = residenceEventMapper.upsert(event);
            logger.info("更新居所事件: {} - 事件数量: {}, 特殊文字: {}, 特殊特效: {}",
                    residence, countEventsInData(eventData), specialText != null, showSpecialEffect);
            return result > 0;
        } catch (Exception e) {
            logger.error("更新居所事件失败，居所: {}", residence, e);
            return false;
        }
    }

    /**
     * 更新居所事件（向后兼容的重载方法）
     *
     * @param residence       居所类型
     * @param eventData       事件数据（JSON格式）
     * @param showHeartEffect 是否显示爱心特效
     * @return 是否更新成功
     */
    public boolean updateResidenceEvent(String residence, String eventData, Boolean showHeartEffect) {
        return updateResidenceEvent(residence, eventData, showHeartEffect, null, false);
    }

    /**
     * 生成居所事件（集成特殊情侣逻辑）
     * 这个方法会被定时任务调用，用于为每个居所生成新的事件
     *
     * @param residence 居所类型
     * @param residents 当前居住人员列表
     * @return 是否生成成功
     */
    public boolean generateResidenceEvent(String residence, List<User> residents) {
        // TODO: 用户手动实现具体的事件生成逻辑
        logger.info("生成居所事件 - 居所: {}, 人员数量: {}", residence, residents != null ? residents.size() : 0);

        // 检查是否是特殊情侣组合
        SpecialCoupleResult specialResult = checkSpecialCouple(residents);

        List<ResidenceEventItem> events = new ArrayList<>();

        if (specialResult.isSpecialCouple) {
            // 特殊情侣组合，生成特殊类型事件
            if (specialResult.isThreePerson) {
                // 三人组合 - 根据居所类型随机选择场景
                List<ResidenceEventItem> randomScene = getRandomThreePersonScene(residence);
                events.addAll(randomScene);
            } else {
                // 两人组合 - 根据居所类型随机选择场景
                List<ResidenceEventItem> randomScene = getRandomTwoPersonScene(residence);
                events.addAll(randomScene);
            }

            // 使用Gson序列化为JSON
            String eventData = gson.toJson(events);
            return updateResidenceEvent(residence, eventData, true, null, true);
        } else {
            // 普通情况，生成普通类型事件
            events.add(new ResidenceEventItem(getResidenceDisplayName(residence) + " 平静如常...", "normal"));
            events.add(new ResidenceEventItem("微风轻拂过" + getResidenceDisplayName(residence), "normal"));

            // 使用Gson序列化为JSON
            String eventData = gson.toJson(events);
            return updateResidenceEvent(residence, eventData, false, null, false);
        }
    }

    /**
     * 检查是否是特殊情侣组合
     *
     * @param residents 居住人员列表
     * @return 特殊情侣检查结果
     */
    private SpecialCoupleResult checkSpecialCouple(List<User> residents) {
        if (residents == null || residents.isEmpty()) {
            return new SpecialCoupleResult(false, false);
        }

        // 提取用户名列表
        List<String> userNames = residents.stream()
                .map(User::getUserId)
                .toList();

        // 检查三人组合：秦小淮、李星斗、存子
        if (userNames.contains("秦小淮") &&
                userNames.contains("李星斗") &&
                userNames.contains("存子")) {
            return new SpecialCoupleResult(true, true);
        }

        // 检查两人组合：秦小淮、李星斗
        if (userNames.contains("秦小淮") &&
                userNames.contains("李星斗")) {
            return new SpecialCoupleResult(true, false);
        }

        return new SpecialCoupleResult(false, false);
    }

    /**
     * 特殊情侣检查结果内部类
     */
    private static class SpecialCoupleResult {
        public final boolean isSpecialCouple;
        public final boolean isThreePerson;

        public SpecialCoupleResult(boolean isSpecialCouple, boolean isThreePerson) {
            this.isSpecialCouple = isSpecialCouple;
            this.isThreePerson = isThreePerson;
        }
    }

    /**
     * 根据居所类型获取随机两人场景
     *
     * @param residence 居所类型
     * @return 随机选择的两人场景
     */
    private List<ResidenceEventItem> getRandomTwoPersonScene(String residence) {
        List<List<ResidenceEventItem>> scenePool;

        switch (residence) {
            case "park":
                // 公园场景：使用公园双人场景池（18个）
                scenePool = List.of(
                        Scenes.TWO__GY__01
                );
                break;
            case "city_hall":
                // 市政厅场景：使用市政厅双人场景池（18个）
                scenePool = List.of(
                        Scenes.TWO__SZT__01, Scenes.TWO__SZT__02
                );
                break;
            default:
                // 城堡等其他场景：使用城堡双人场景池（18个）
                scenePool = List.of(
                        Scenes.TWO__CB__01,
                        Scenes.TWO__CB__02,
                        Scenes.TWO__CB__03,
                        Scenes.TWO__CB__04,
                        Scenes.TWO__CB__05,
                        Scenes.TWO__CB__06,
                        Scenes.TWO__CB__07,
                        Scenes.TWO__CB__08,
                        Scenes.TWO__CB__09,
                        Scenes.TWO__CB__10,
                        Scenes.TWO__CB__11,
                        Scenes.TWO__CB__12,
                        Scenes.TWO__CB__13
                );
                break;
        }

        return scenePool.get((int) (Math.random() * scenePool.size()));
    }

    /**
     * 根据居所类型获取随机三人场景
     *
     * @param residence 居所类型
     * @return 随机选择的三人场景
     */
    private List<ResidenceEventItem> getRandomThreePersonScene(String residence) {
        List<List<ResidenceEventItem>> scenePool = switch (residence) {
            case "park" ->
                // 公园场景：使用公园三人场景池（18个）
                    List.of(
                            Scenes.THREE__GY__02, Scenes.THREE__GY__03, Scenes.THREE__GY__08
                    );
            case "city_hall" ->
                // 市政厅场景：使用市政厅三人场景池（18个）
                    List.of(
                            Scenes.THREE__SZT__06
                    );
            default ->
                // 城堡等其他场景：使用城堡三人场景池（18个）
                    List.of(
                            Scenes.THREE__CBS__02
                    );
        };

        return scenePool.get((int) (Math.random() * scenePool.size()));
    }

    /**
     * 获取居所显示名称
     *
     * @param residence 居所类型
     * @return 显示名称
     */
    private String getResidenceDisplayName(String residence) {
        return switch (residence) {
            case "castle" -> "🏰 城堡";
            case "city_hall" -> "🏛️ 市政厅";
            case "palace" -> "🏯 行宫";
            case "dove_house" -> "🕊️ 小白鸽家";
            case "park" -> "🌳 公园";
            default -> "未知居所";
        };
    }

    /**
     * 刷新所有居所的事件
     *
     * @return 刷新成功的居所数量
     */
    public int refreshAllResidenceEvents() {
        String[] residences = {"castle", "city_hall", "palace", "dove_house", "park"};
        int successCount = 0;

        for (String residence : residences) {
            try {
                // 获取该居所的当前居住人员
                List<User> residents = userMapper.selectByResidence(residence);

                // 生成新的事件
                if (generateResidenceEvent(residence, residents)) {
                    successCount++;
                }
            } catch (Exception e) {
                logger.error("刷新居所事件失败，居所: {}", residence, e);
            }
        }

        logger.info("居所事件刷新完成，成功: {}/5", successCount);
        return successCount;
    }

    /**
     * 获取居所的居住人员信息
     *
     * @param residence 居所类型
     * @return 居住人员列表
     */
    public List<User> getResidenceResidents(String residence) {
        try {
            return userMapper.selectByResidence(residence);
        } catch (Exception e) {
            logger.error("获取居所居住人员失败，居所: {}", residence, e);
            return null;
        }
    }

    /**
     * 统计事件数据中的事件数量
     *
     * @param eventData JSON格式的事件数据
     * @return 事件数量
     */
    private int countEventsInData(String eventData) {
        if (eventData == null || eventData.trim().isEmpty() || eventData.equals("[]")) {
            return 0;
        }

        try {
            // 简单的JSON数组计数（计算逗号分隔的对象数量）
            int count = 1;
            for (char c : eventData.toCharArray()) {
                if (c == '{') {
                    if (count == 1) count = 1; // 第一个对象
                    else count++; // 后续对象
                }
            }
            return Math.max(count - 1, 0); // 减去初始值1
        } catch (Exception e) {
            return 1; // 默认返回1个事件
        }
    }
}
