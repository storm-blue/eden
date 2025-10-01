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
     * 获取用户信息（不自动创建）
     * @param userId 用户ID（姓名）
     * @return 用户信息，如果不存在返回null
     */
    public User getUserById(String userId) {
        return userMapper.selectByUserId(userId);
    }
    
    /**
     * 创建新用户
     * @param userId 用户ID（姓名）
     * @param dailyDraws 每日抽奖次数，如果为null则随机分配1-5次
     * @return 创建的用户信息
     */
    @Transactional
    public User createUser(String userId, Integer dailyDraws) {
        // 检查用户是否已存在
        User existingUser = userMapper.selectByUserId(userId);
        if (existingUser != null) {
            logger.warn("用户 {} 已存在，无法重复创建", userId);
            return existingUser;
        }
        
        // 如果没有指定每日抽奖次数，则随机分配（1-5次）
        if (dailyDraws == null || dailyDraws <= 0) {
            dailyDraws = random.nextInt(5) + 1;
        }
        
        User user = new User(userId, dailyDraws);
        userMapper.insert(user);
        
        logger.info("创建新用户: {}, 每日抽奖次数: {}", userId, dailyDraws);
        return user;
    }
    
    /**
     * 检查用户是否可以抽奖
     * @param userId 用户ID
     * @return 是否可以抽奖
     */
    public boolean canDraw(String userId) {
        User user = getUserById(userId);
        if (user == null) {
            logger.info("用户 {} 不存在，无法抽奖", userId);
            return false;
        }
        return user.getRemainingDraws() > 0;
    }
    
    /**
     * 获取用户剩余抽奖次数
     * @param userId 用户ID
     * @return 剩余次数，如果用户不存在返回0
     */
    public int getRemainingDraws(String userId) {
        User user = getUserById(userId);
        if (user == null) {
            return 0;
        }
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
     * @return 用户信息，如果不存在返回null
     */
    public User getUserInfo(String userId) {
        return getUserById(userId);
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
     * @return 是否更新成功
     */
    @Transactional
    public boolean updateUserDailyDraws(String userId, Integer dailyDraws) {
        User user = getUserById(userId);
        if (user == null) {
            logger.error("用户 {} 不存在，无法更新每日抽奖次数", userId);
            return false;
        }
        
        user.setDailyDraws(dailyDraws);
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.update(user);
        logger.info("更新用户 {} 的每日抽奖次数为: {}", userId, dailyDraws);
        return true;
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
            
            User user = getUserById(userId);
            if (user == null) {
                logger.error("用户 {} 不存在，无法增加抽奖次数", userId);
                return false;
            }
            
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
