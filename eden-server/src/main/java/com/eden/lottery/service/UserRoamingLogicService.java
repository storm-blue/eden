package com.eden.lottery.service;

import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户漫游逻辑服务
 * 这个类包含用户自定义的漫游逻辑
 * 用户可以在这里实现具体的漫游算法
 */
@Service
public class UserRoamingLogicService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoamingLogicService.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 确定用户的新居所
     *
     * @param username         用户名
     * @param currentResidence 当前居所（如：castle, park, city_hall, white_dove_house, palace）
     * @return 新居所名称，如果不需要移动则返回null或当前居所
     */
    public String determineNewResidence(String username, String currentResidence) {
        logger.debug("为用户 {} 确定新居所，当前居所: {}", username, currentResidence);

        // 小白鸽可以去所有地方，随机移动
        if ("小白鸽".equals(username)) {
            return performRandomMove(username, currentResidence);
        }

        // 存子的特殊移动逻辑
        if ("存子".equals(username)) {
            return performCunziMove(username, currentResidence);
        }

        // 白婆婆的特殊移动逻辑
        if ("白婆婆".equals(username)) {
            return performBaipopoMove(username, currentResidence);
        }

        // 大祭祀的特殊移动逻辑
        if ("大祭祀".equals(username)) {
            return performDajiziMove(username, currentResidence);
        }

        // 严伯升的特殊移动逻辑
        if ("严伯升".equals(username)) {
            return performYanboshengMove(username, currentResidence);
        }

        // 其他用户不移动
        logger.debug("用户 {} 保持在当前居所: {}", username, getResidenceDisplayName(currentResidence));
        return null;
    }

    /**
     * 执行随机移动（适用于小白鸽）
     */
    private String performRandomMove(String username, String currentResidence) {
        // 获取所有可用居所
        String[] availableResidences = getAvailableResidences();

        List<String> targetResidences = new ArrayList<>(Arrays.asList(availableResidences));

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

            // 如果目标居所和当前居所一样，不移动
            if (currentResidence.equals(newResidence)) {
                return null;
            }

            logger.info("用户 {} 将从 {} 移动到 {}", username,
                    getResidenceDisplayName(currentResidence),
                    getResidenceDisplayName(newResidence));

            return newResidence;
        }

        return null;
    }

    /**
     * 执行存子的移动逻辑
     */
    private String performCunziMove(String username, String currentResidence) {
        try {
            // 获取当前居所中的所有用户
            List<User> currentResidents = userMapper.selectByResidence(currentResidence);

            // 检查秦小淮和李星斗是否在当前居所
            boolean hasQinXiaohuai = currentResidents.stream()
                    .anyMatch(user -> "秦小淮".equals(user.getUserId()));
            boolean hasLiXingdou = currentResidents.stream()
                    .anyMatch(user -> "李星斗".equals(user.getUserId()));

            double moveChance = 0.0;
            String logMessage = "";

            if (hasQinXiaohuai && hasLiXingdou) {
                // 如果秦小淮和李星斗都在，移动概率为10%
                moveChance = 0.10;
                logMessage = "秦小淮和李星斗都在当前居所，移动概率: 10%";
            } else if (hasQinXiaohuai || hasLiXingdou) {
                // 如果秦小淮和李星斗任意一个人在，移动概率为30%
                moveChance = 0.30;
                String presentPerson = hasQinXiaohuai ? "秦小淮" : "李星斗";
                logMessage = String.format("%s在当前居所，移动概率: 30%%", presentPerson);
            } else {
                // 如果秦小淮和李星斗都不在，按照原逻辑随机移动
                moveChance = 1.0;
                logMessage = "秦小淮和李星斗都不在当前居所，正常随机移动";
            }

            logger.info("存子移动逻辑: {} (当前居所: {})", logMessage, getResidenceDisplayName(currentResidence));

            // 根据概率决定是否移动
            double random = Math.random();
            if (random < moveChance) {
                // 获取所有可用居所
                String[] availableResidences = getAvailableResidences();

                // 过滤掉当前居所，避免"移动"到相同位置
                List<String> targetResidences = new ArrayList<>(Arrays.asList(availableResidences));
                targetResidences.remove(currentResidence);

                // 如果有可选的居所，随机选择一个
                if (!targetResidences.isEmpty()) {
                    int randomIndex = (int) (Math.random() * targetResidences.size());
                    String newResidence = targetResidences.get(randomIndex);

                    logger.info("存子将从 {} 移动到 {} (随机值: {}, 阈值: {})",
                            getResidenceDisplayName(currentResidence),
                            getResidenceDisplayName(newResidence),
                            random, moveChance);

                    return newResidence;
                }
            } else {
                logger.info("存子不移动 (随机值: {}, 阈值: {})", random, moveChance);
            }

        } catch (Exception e) {
            logger.error("查询存子移动逻辑时发生错误: {}", e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取所有可用的居所列表
     *
     * @return 所有可用居所的列表
     */
    public String[] getAvailableResidences() {
        return new String[]{
                "castle",           // 城堡🏰
                "park",             // 公园🌳
                "city_hall",        // 市政厅🏛️
                "white_dove_house", // 小白鸽家🕊️
                "palace"            // 行宫🏯
        };
    }

    /**
     * 获取居所的显示名称
     *
     * @param residence 居所key
     * @return 居所的显示名称
     */
    public String getResidenceDisplayName(String residence) {
        return switch (residence) {
            case "castle" -> "城堡🏰";
            case "park" -> "公园🌳";
            case "city_hall" -> "市政厅🏛️";
            case "white_dove_house" -> "小白鸽家🕊️";
            case "palace" -> "行宫🏯";
            default -> residence;
        };
    }

    /**
     * 执行白婆婆的移动逻辑
     * 白婆婆可能去公园、小白鸽家
     */
    private String performBaipopoMove(String username, String currentResidence) {
        logger.debug("执行白婆婆的移动逻辑，当前居所: {}", getResidenceDisplayName(currentResidence));

        // 白婆婆的可选居所：公园、小白鸽家
        String[] preferredResidences = {"park", "white_dove_house"};

        List<String> targetResidences = new ArrayList<>(Arrays.asList(preferredResidences));

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

            if (currentResidence.equals(newResidence)) {
                return null;
            }

            logger.info("白婆婆将从 {} 移动到 {}",
                    getResidenceDisplayName(currentResidence),
                    getResidenceDisplayName(newResidence));

            return newResidence;
        }

        logger.debug("白婆婆没有可移动的居所，保持当前位置");
        return null;
    }

    /**
     * 执行大祭祀的移动逻辑
     * 大祭祀可能去行宫、城堡、公园
     */
    private String performDajiziMove(String username, String currentResidence) {
        logger.debug("执行大祭祀的移动逻辑，当前居所: {}", getResidenceDisplayName(currentResidence));

        // 大祭祀的可选居所：行宫、城堡、公园
        String[] preferredResidences = {"palace", "castle", "park"};

        // 过滤掉当前居所
        List<String> targetResidences = new ArrayList<>(Arrays.asList(preferredResidences));

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

            if (currentResidence.equals(newResidence)) {
                return null;
            }

            logger.info("大祭祀将从 {} 移动到 {}",
                    getResidenceDisplayName(currentResidence),
                    getResidenceDisplayName(newResidence));

            return newResidence;
        }

        logger.debug("大祭祀没有可移动的居所，保持当前位置");
        return null;
    }

    /**
     * 执行严伯升的移动逻辑
     * 严伯升可能去城堡、市政厅
     */
    private String performYanboshengMove(String username, String currentResidence) {
        logger.debug("执行严伯升的移动逻辑，当前居所: {}", getResidenceDisplayName(currentResidence));

        // 严伯升的可选居所：城堡、市政厅
        String[] preferredResidences = {"castle", "city_hall"};

        // 过滤掉当前居所
        List<String> targetResidences = new ArrayList<>(Arrays.asList(preferredResidences));

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

            if (currentResidence.equals(newResidence)) {
                return null;
            }

            logger.info("严伯升将从 {} 移动到 {}",
                    getResidenceDisplayName(currentResidence),
                    getResidenceDisplayName(newResidence));

            return newResidence;
        }

        logger.debug("严伯升没有可移动的居所，保持当前位置");
        return null;
    }
}
