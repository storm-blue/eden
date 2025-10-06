package com.eden.lottery.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @param username 用户名
     * @param currentResidence 当前居所（如：castle, park, city_hall, white_dove_house, palace）
     * @return 新居所名称，如果不需要移动则返回null或当前居所
     */
    public String determineNewResidence(String username, String currentResidence) {
        logger.debug("为用户 {} 确定新居所，当前居所: {}", username, currentResidence);
        
        // TODO: 在这里实现你的漫游逻辑
        // 
        // 示例逻辑思路：
        // 1. 根据用户特征（用户名、历史行为等）决定移动概率
        // 2. 根据当前居所和时间因素选择目标居所
        // 3. 考虑居所容量限制
        // 4. 实现特殊规则（如某些用户倾向于特定居所）
        //
        // 参数说明：
        // - username: 用户名，可以用来实现个性化逻辑
        // - currentResidence: 当前居所，可选值：
        //   * "castle" - 城堡🏰
        //   * "park" - 公园🌳  
        //   * "city_hall" - 市政厅🏛️
        //   * "white_dove_house" - 小白鸽家🕊️
        //   * "palace" - 行宫🏯
        //
        // 返回值：
        // - 返回新居所名称（上述可选值之一）表示需要移动
        // - 返回null或currentResidence表示不移动
        
        // 临时实现：返回null表示暂不移动任何用户
        // 用户可以根据需要修改这里的逻辑
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
            case "castle": return "城堡🏰";
            case "park": return "公园🌳";
            case "city_hall": return "市政厅🏛️";
            case "white_dove_house": return "小白鸽家🕊️";
            case "palace": return "行宫🏯";
            default: return residence;
        }
    }
}
