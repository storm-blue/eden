package com.eden.lottery.service;

import com.eden.lottery.entity.User;
import com.eden.lottery.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserMapper userMapper;
    
    private final Random random = new Random();
    
    /**
     * 获取或创建用户
     * @param userId 用户ID（姓名）
     * @return 用户信息
     */
    @Transactional
    public User getOrCreateUser(String userId) {
        User user = userMapper.selectByUserId(userId);
        
        if (user == null) {
            // 新用户，随机分配每日抽奖次数（1-5次）
            int randomDailyDraws = random.nextInt(5) + 1;
            user = new User(userId, randomDailyDraws);
            
            userMapper.insert(user);
            logger.info("创建新用户: {}, 每日抽奖次数: {}", userId, randomDailyDraws);
        }
        
        return user;
    }
    
    /**
     * 检查用户是否可以抽奖
     * @param userId 用户ID
     * @return 是否可以抽奖
     */
    public boolean canDraw(String userId) {
        User user = getOrCreateUser(userId);
        return user.getRemainingDraws() > 0;
    }
    
    /**
     * 获取用户剩余抽奖次数
     * @param userId 用户ID
     * @return 剩余次数
     */
    public int getRemainingDraws(String userId) {
        User user = getOrCreateUser(userId);
        return user.getRemainingDraws();
    }
    
    /**
     * 扣减用户抽奖次数
     * @param userId 用户ID
     * @return 是否扣减成功
     */
    @Transactional
    public boolean decreaseDraws(String userId) {
        try {
            userMapper.decreaseRemainingDraws(userId);
            logger.info("用户 {} 抽奖次数扣减成功", userId);
            return true;
        } catch (Exception e) {
            logger.error("用户 {} 抽奖次数扣减失败: {}", userId, e.getMessage());
            return false;
        }
    }
    
    /**
     * 刷新单个用户的每日抽奖次数
     * @param userId 用户ID
     */
    @Transactional
    public void refreshUserDailyDraws(String userId) {
        try {
            userMapper.refreshDailyDraws(userId, LocalDateTime.now());
            logger.info("用户 {} 每日抽奖次数刷新成功", userId);
        } catch (Exception e) {
            logger.error("用户 {} 每日抽奖次数刷新失败: {}", userId, e.getMessage());
        }
    }
    
    /**
     * 批量刷新所有用户的每日抽奖次数
     */
    @Transactional
    public void batchRefreshDailyDraws() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
            
            // 查找需要刷新的用户
            List<User> usersNeedRefresh = userMapper.selectUsersNeedRefresh(todayStart);
            
            if (!usersNeedRefresh.isEmpty()) {
                // 批量刷新
                userMapper.batchRefreshDailyDraws(now);
                logger.info("批量刷新了 {} 个用户的每日抽奖次数", usersNeedRefresh.size());
            } else {
                logger.info("没有用户需要刷新每日抽奖次数");
            }
        } catch (Exception e) {
            logger.error("批量刷新用户每日抽奖次数失败: {}", e.getMessage());
        }
    }
    
    /**
     * 获取用户详细信息
     * @param userId 用户ID
     * @return 用户信息
     */
    public User getUserInfo(String userId) {
        return getOrCreateUser(userId);
    }
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }
    
    /**
     * 更新用户每日抽奖次数
     * @param userId 用户ID
     * @param dailyDraws 新的每日抽奖次数
     */
    @Transactional
    public void updateUserDailyDraws(String userId, Integer dailyDraws) {
        User user = getOrCreateUser(userId);
        user.setDailyDraws(dailyDraws);
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.update(user);
        logger.info("更新用户 {} 的每日抽奖次数为: {}", userId, dailyDraws);
    }
    
    /**
     * 增加用户剩余抽奖次数（抽到"再转一次"时使用）
     * @param userId 用户ID
     * @param amount 增加的次数，默认为1
     * @return 是否增加成功
     */
    @Transactional
    public boolean increaseRemainingDraws(String userId, Integer amount) {
        try {
            if (amount == null || amount <= 0) {
                amount = 1; // 默认增加1次
            }
            
            User user = getOrCreateUser(userId);
            user.setRemainingDraws(user.getRemainingDraws() + amount);
            user.setUpdateTime(LocalDateTime.now());
            
            userMapper.update(user);
            logger.info("用户 {} 剩余抽奖次数增加 {} 次，当前剩余: {} 次", 
                       userId, amount, user.getRemainingDraws());
            return true;
        } catch (Exception e) {
            logger.error("用户 {} 抽奖次数增加失败: {}", userId, e.getMessage());
            return false;
        }
    }
}
