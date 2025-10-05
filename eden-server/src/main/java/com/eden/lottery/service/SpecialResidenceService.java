package com.eden.lottery.service;

import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 特殊居住组合检测服务
 */
@Service
public class SpecialResidenceService {
    
    private static final Logger logger = LoggerFactory.getLogger(SpecialResidenceService.class);
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 特殊组合定义
     */
    public enum SpecialCombo {
        COUPLE_COMBO(1000, "李星斗", "秦小淮"),           // 两人组合：每小时+1000人口
        TRIPLE_COMBO(1500, "李星斗", "秦小淮", "存子");    // 三人组合：每小时+1500人口
        
        private final int hourlyBonus;
        private final String[] members;
        
        SpecialCombo(int hourlyBonus, String... members) {
            this.hourlyBonus = hourlyBonus;
            this.members = members;
        }
        
        public int getHourlyBonus() {
            return hourlyBonus;
        }
        
        public String[] getMembers() {
            return members;
        }
        
        public String getDescription() {
            if (members.length == 2) {
                return String.format("%s和%s的爱情加成", members[0], members[1]);
            } else if (members.length == 3) {
                return String.format("%s、%s和%s的三人组合加成", members[0], members[1], members[2]);
            }
            return "特殊组合加成";
        }
    }
    
    /**
     * 检测当前是否有特殊居住组合，并返回总的人口增长加成
     * @return 每小时人口增长加成
     */
    public int calculateHourlyPopulationBonus() {
        try {
            int totalBonus = 0;
            
            // 获取所有有居住地点的用户，按居住地点分组
            List<User> usersWithResidence = userMapper.selectAllWithResidence();
            Map<String, List<User>> residenceGroups = usersWithResidence.stream()
                    .collect(Collectors.groupingBy(User::getResidence));
            
            // 检查每个居住地点是否有特殊组合
            for (Map.Entry<String, List<User>> entry : residenceGroups.entrySet()) {
                String residence = entry.getKey();
                List<User> residents = entry.getValue();
                
                SpecialCombo combo = detectSpecialCombo(residents);
                if (combo != null) {
                    totalBonus += combo.getHourlyBonus();
                    logger.info("检测到特殊居住组合：{} 在 {} - 人口加成: +{}/小时", 
                               combo.getDescription(), getResidenceName(residence), combo.getHourlyBonus());
                }
            }
            
            if (totalBonus > 0) {
                logger.info("当前特殊居住组合总人口加成：+{}/小时", totalBonus);
            }
            
            return totalBonus;
        } catch (Exception e) {
            logger.error("计算特殊居住组合加成失败", e);
            return 0;
        }
    }
    
    /**
     * 检测一个居住地点的用户是否构成特殊组合
     * @param residents 居住者列表
     * @return 特殊组合类型，如果没有则返回null
     */
    private SpecialCombo detectSpecialCombo(List<User> residents) {
        if (residents == null || residents.isEmpty()) {
            return null;
        }
        
        List<String> userIds = residents.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        
        // 检查三人组合（优先级更高）
        if (userIds.size() == 3 && 
            userIds.contains("李星斗") && 
            userIds.contains("秦小淮") && 
            userIds.contains("存子")) {
            return SpecialCombo.TRIPLE_COMBO;
        }
        
        // 检查两人组合
        if (userIds.size() == 2 && 
            userIds.contains("李星斗") && 
            userIds.contains("秦小淮")) {
            return SpecialCombo.COUPLE_COMBO;
        }
        
        return null;
    }
    
    /**
     * 获取当前所有特殊组合的详细信息
     * @return 特殊组合信息列表
     */
    public List<Map<String, Object>> getActiveSpecialCombos() {
        try {
            List<User> usersWithResidence = userMapper.selectAllWithResidence();
            Map<String, List<User>> residenceGroups = usersWithResidence.stream()
                    .collect(Collectors.groupingBy(User::getResidence));
            
            return residenceGroups.entrySet().stream()
                    .map(entry -> {
                        String residence = entry.getKey();
                        List<User> residents = entry.getValue();
                        SpecialCombo combo = detectSpecialCombo(residents);
                        
                        if (combo != null) {
                            return Map.of(
                                "residence", residence,
                                "residenceName", getResidenceName(residence),
                                "combo", combo.name(),
                                "description", combo.getDescription(),
                                "hourlyBonus", combo.getHourlyBonus(),
                                "members", residents.stream().map(User::getUserId).collect(Collectors.toList())
                            );
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("获取特殊组合信息失败", e);
            return List.of();
        }
    }
    
    /**
     * 获取居住地点的中文名称
     */
    private String getResidenceName(String residence) {
        if (residence == null) return "未知地点";
        
        switch (residence) {
            case "castle": return "🏰 城堡";
            case "city_hall": return "🏛️ 市政厅";
            case "palace": return "🏯 行宫";
            case "dove_house": return "🕊️ 小白鸽家";
            case "park": return "🌳 公园";
            default: return residence;
        }
    }
}
