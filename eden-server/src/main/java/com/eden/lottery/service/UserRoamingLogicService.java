package com.eden.lottery.service;

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

    /**
     * 确定用户的新居所
     *
     * @param username         用户名
     * @param currentResidence 当前居所（如：castle, park, city_hall, white_dove_house, palace）
     * @return 新居所名称，如果不需要移动则返回null或当前居所
     */
    public String determineNewResidence(String username, String currentResidence) {
        logger.debug("为用户 {} 确定新居所，当前居所: {}", username, currentResidence);

        // "小白鸽" 可以去所有地方
        if ("存子".equals(username) || "小白鸽".equals(username)) {
            // 获取所有可用居所
            String[] availableResidences = getAvailableResidences();

            // 过滤掉当前居所，避免"移动"到相同位置
            List<String> targetResidences = new ArrayList<>(Arrays.asList(availableResidences));

            // 如果有可选的居所，随机选择一个
            if (!targetResidences.isEmpty()) {
                int randomIndex = (int) (Math.random() * targetResidences.size());
                String newResidence = targetResidences.get(randomIndex);

                logger.info("用户 {} 将从 {} 移动到 {}", username,
                        getResidenceDisplayName(currentResidence),
                        getResidenceDisplayName(newResidence));

                return newResidence;
            }
        }

        if ("存子".equals(username)) {
            // TODO 如果当前居所中秦小淮和李星斗任意一个人在，移动的概率为30%。如果秦小淮和李星斗都在，移动的概率为10%。
        }

        // 其他用户或没有可移动的居所时，不移动
        logger.debug("用户 {} 保持在当前居所: {}", username, getResidenceDisplayName(currentResidence));
        return null;
    }

    /**
     * 检查用户是否应该参与漫游
     *
     * @param username 用户名
     * @return true表示参与漫游，false表示跳过
     */
    public boolean shouldUserParticipateInRoaming(String username) {
        // TODO: 在这里实现用户筛选逻辑
        // 例如：某些特殊用户可能不参与自动漫游

        // 默认所有用户都参与漫游
        return true;
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
        switch (residence) {
            case "castle":
                return "城堡🏰";
            case "park":
                return "公园🌳";
            case "city_hall":
                return "市政厅🏛️";
            case "white_dove_house":
                return "小白鸽家🕊️";
            case "palace":
                return "行宫🏯";
            default:
                return residence;
        }
    }
}
