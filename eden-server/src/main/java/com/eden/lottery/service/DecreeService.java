package com.eden.lottery.service;

import com.eden.lottery.entity.Decree;
import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.DecreeMapper;
import com.eden.lottery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 命令服务
 */
@Service
public class DecreeService {
    
    private static final Logger logger = LoggerFactory.getLogger(DecreeService.class);
    
    @Autowired
    private DecreeMapper decreeMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StarCityService starCityService;
    
    // 命令代码常量
    public static final String DECREE_NO_CASTLE_ACCESS = "NO_CASTLE_ACCESS";
    public static final String DECREE_CREATE_RAINBOW = "CREATE_RAINBOW";
    
    /**
     * 获取所有命令
     */
    public List<Decree> getAllDecrees() {
        return decreeMapper.selectAll();
    }
    
    /**
     * 颁布命令
     */
    @Transactional
    public void issueDecree(String code, String userId) {
        // 验证用户权限（仅秦小淮可颁布命令）
        if (!"秦小淮".equals(userId)) {
            throw new IllegalArgumentException("只有秦小淮可以颁布命令");
        }
        
        Decree decree = decreeMapper.selectByCode(code);
        if (decree == null) {
            throw new IllegalArgumentException("命令不存在");
        }
        
        if (decree.getActive()) {
            logger.warn("命令已经处于激活状态: {}", code);
            return;
        }
        
        // 激活命令
        decree.setActive(true);
        decree.setIssuedAt(LocalDateTime.now());
        decree.setCancelledAt(null);
        decree.setIssuedBy(userId);
        decreeMapper.update(decree);
        
        logger.info("命令已颁布: {} by {}", code, userId);
        
        // 执行命令效果
        executeDecreeEffect(code);
    }
    
    /**
     * 取消命令
     */
    @Transactional
    public void cancelDecree(String code, String userId) {
        // 验证用户权限
        if (!"秦小淮".equals(userId)) {
            throw new IllegalArgumentException("只有秦小淮可以取消命令");
        }
        
        Decree decree = decreeMapper.selectByCode(code);
        if (decree == null) {
            throw new IllegalArgumentException("命令不存在");
        }
        
        if (!decree.getActive()) {
            logger.warn("命令已经处于未激活状态: {}", code);
            return;
        }
        
        // 取消命令
        decree.setActive(false);
        decree.setCancelledAt(LocalDateTime.now());
        decreeMapper.update(decree);
        
        logger.info("命令已取消: {} by {}", code, userId);
    }
    
    /**
     * 检查命令是否激活
     */
    public boolean isDecreeActive(String code) {
        return decreeMapper.isActive(code);
    }
    
    /**
     * 更新命令信息（仅名称和描述）
     * 管理员使用，不影响命令的激活状态
     */
    public void updateDecreeInfo(String code, String name, String description) {
        Decree decree = decreeMapper.selectByCode(code);
        if (decree == null) {
            throw new IllegalArgumentException("命令不存在");
        }
        
        decree.setName(name);
        decree.setDescription(description);
        // 只更新名称和描述，保持其他字段不变
        decreeMapper.updateInfo(decree);
        
        logger.info("更新命令信息: code={}, name={}, description={}", code, name, description);
    }
    
    /**
     * 执行命令效果
     */
    private void executeDecreeEffect(String code) {
        if (DECREE_NO_CASTLE_ACCESS.equals(code)) {
            executeNoCastleAccessDecree();
        } else if (DECREE_CREATE_RAINBOW.equals(code)) {
            executeCreateRainbowDecree();
        }
    }
    
    /**
     * 执行"创造彩虹"命令效果
     * 纯前端视觉效果，后端仅记录日志
     */
    private void executeCreateRainbowDecree() {
        logger.info("执行命令效果：创造彩虹 - 星星城将显示彩虹特效");
    }
    
    /**
     * 执行"不得靠近城堡"命令效果
     * 驱逐城堡中除了李星斗之外的所有人
     */
    private void executeNoCastleAccessDecree() {
        logger.info("执行命令效果：不得靠近城堡 - 驱逐城堡中的所有人（除了李星斗）");
        
        // 定义目标居所映射
        String[][] evictions = {
            {"存子", "palace"},           // 存子 -> 行宫
            {"小白鸽", "white_dove_house"}, // 小白鸽 -> 小白鸽家
            {"白婆婆", "white_dove_house"}, // 白婆婆 -> 小白鸽家
            {"大祭司", "palace"},          // 大祭司 -> 行宫
            {"严伯升", "city_hall"}        // 严伯升 -> 市政厅
        };
        
        int evictedCount = 0;
        
        for (String[] eviction : evictions) {
            String userName = eviction[0];
            String targetResidence = eviction[1];
            
            // 检查用户当前是否在城堡
            User user = userMapper.selectByUserId(userName);
            if (user != null && "castle".equals(user.getResidence())) {
                // 使用统一的移动方法驱逐用户（自动刷新事件）
                boolean moveSuccess = starCityService.moveUserToBuilding(
                    userName, 
                    "castle", 
                    targetResidence, 
                    "decree"  // 标记为命令驱逐
                );
                
                if (moveSuccess) {
                    evictedCount++;
                    logger.info("驱逐用户：{} 从城堡到 {}", userName, targetResidence);
                } else {
                    logger.warn("驱逐用户 {} 失败", userName);
                }
            }
        }
        
        if (evictedCount > 0) {
            logger.info("命令效果执行完毕，共驱逐 {} 名用户，相关居所事件已自动刷新", evictedCount);
        } else {
            logger.info("城堡中没有需要驱逐的用户");
        }
    }
}

