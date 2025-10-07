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

        return switch (username) {
            case "小白鸽" -> performSimpleRandomMove(username, currentResidence, getAllResidences());
            case "存子" -> performCunziMove(username, currentResidence);
            case "白婆婆" -> performSimpleRandomMove(username, currentResidence, new String[]{"park", "white_dove_house"});
            case "大祭祀" -> performSimpleRandomMove(username, currentResidence, new String[]{"palace", "castle", "park"});
            case "严伯升" -> performSimpleRandomMove(username, currentResidence, new String[]{"castle", "city_hall"});
            default -> {
                logger.debug("用户 {} 保持在当前居所: {}", username, getResidenceDisplayName(currentResidence));
                yield null;
            }
        };
    }

    /**
     * 执行简单的随机移动逻辑
     * 适用于大部分用户的移动逻辑
     *
     * @param username 用户名
     * @param currentResidence 当前居所
     * @param availableResidences 可选居所列表
     * @return 新居所名称，如果不移动则返回null
     */
    private String performSimpleRandomMove(String username, String currentResidence, String[] availableResidences) {
        logger.debug("执行{}的移动逻辑，当前居所: {}", username, getResidenceDisplayName(currentResidence));

        // 过滤掉当前居所，避免"移动"到相同位置
        List<String> targetResidences = new ArrayList<>(Arrays.asList(availableResidences));
        targetResidences.remove(currentResidence);

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

            logger.info("{}将从 {} 移动到 {}", username,
                    getResidenceDisplayName(currentResidence),
                    getResidenceDisplayName(newResidence));

            return newResidence;
        }

        logger.debug("{}没有可移动的居所，保持当前位置", username);
        return null;
    }

    /**
     * 执行存子的移动逻辑
     * 基于秦小淮和李星斗的位置决定移动概率
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

            // 根据情况确定移动概率
            double moveChance;
            String logMessage;
            
            if (hasQinXiaohuai && hasLiXingdou) {
                moveChance = 0.10;
                logMessage = "秦小淮和李星斗都在当前居所，移动概率: 10%";
            } else if (hasQinXiaohuai || hasLiXingdou) {
                moveChance = 0.30;
                String presentPerson = hasQinXiaohuai ? "秦小淮" : "李星斗";
                logMessage = String.format("%s在当前居所，移动概率: 30%%", presentPerson);
            } else {
                moveChance = 1.0;
                logMessage = "秦小淮和李星斗都不在当前居所，正常随机移动";
            }

            logger.info("存子移动逻辑: {} (当前居所: {})", logMessage, getResidenceDisplayName(currentResidence));

            // 根据概率决定是否移动
            double random = Math.random();
            if (random < moveChance) {
                // 使用统一的随机移动逻辑
                String newResidence = performSimpleRandomMove(username, currentResidence, getAllResidences());
                
                if (newResidence != null) {
                    logger.info("存子移动决策成功 (随机值: {}, 阈值: {})", random, moveChance);
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
    public String[] getAllResidences() {
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
}
