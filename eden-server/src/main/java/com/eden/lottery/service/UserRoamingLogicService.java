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
            // TODO 白婆婆可能去公园、小白鸽家
        }

        // 大祭祀的特殊移动逻辑
        if ("大祭祀".equals(username)) {
            // TODO 大祭司可能去行宫、城堡、公园
        }

        // 严伯升的特殊移动逻辑
        if ("严伯升".equals(username)) {
            // TODO 严伯升可能去城堡、市政厅
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

        // 过滤掉当前居所，避免"移动"到相同位置
        List<String> targetResidences = new ArrayList<>(Arrays.asList(availableResidences));
        targetResidences.remove(currentResidence);

        // 如果有可选的居所，随机选择一个
        if (!targetResidences.isEmpty()) {
            int randomIndex = (int) (Math.random() * targetResidences.size());
            String newResidence = targetResidences.get(randomIndex);

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
                    
                    logger.info("存子将从 {} 移动到 {} (随机值: {:.3f}, 阈值: {:.3f})", 
                            getResidenceDisplayName(currentResidence),
                            getResidenceDisplayName(newResidence),
                            random, moveChance);
                    
                    return newResidence;
                }
            } else {
                logger.info("存子不移动 (随机值: {:.3f}, 阈值: {:.3f})", random, moveChance);
            }
            
        } catch (Exception e) {
            logger.error("查询存子移动逻辑时发生错误: {}", e.getMessage(), e);
        }
        
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
