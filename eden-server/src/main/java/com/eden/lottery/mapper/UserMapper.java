package com.eden.lottery.mapper;

import com.eden.lottery.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 插入用户
     */
    void insert(User user);
    
    /**
     * 根据用户ID查询用户
     */
    User selectByUserId(@Param("userId") String userId);
    
    /**
     * 更新用户信息
     */
    void update(User user);
    
    /**
     * 扣减用户抽奖次数
     */
    void decreaseRemainingDraws(@Param("userId") String userId);
    
    /**
     * 增加用户许愿次数
     */
    void increaseWishCount(@Param("userId") String userId);
    
    /**
     * 扣减用户许愿次数
     */
    void decreaseWishCount(@Param("userId") String userId);
    
    /**
     * 更新用户居住地点
     */
    void updateResidence(@Param("userId") String userId, @Param("residence") String residence);
    
    /**
     * 更新用户头像
     */
    void updateAvatar(@Param("userId") String userId, @Param("avatarPath") String avatarPath);
    
    /**
     * 更新用户简介
     */
    void updateProfile(@Param("userId") String userId, @Param("profile") String profile);
    
    /**
     * 更新用户状态
     */
    void updateStatus(@Param("userId") String userId, @Param("status") String status);
    
    /**
     * 更新用户简介和状态
     */
    void updateProfileAndStatus(@Param("userId") String userId, @Param("profile") String profile, @Param("status") String status);
    
    /**
     * 根据居住地点查询用户列表
     */
    List<User> selectByResidence(@Param("residence") String residence);
    
    /**
     * 刷新用户每日抽奖次数
     */
    void refreshDailyDraws(@Param("userId") String userId, @Param("refreshTime") LocalDateTime refreshTime);
    
    /**
     * 获取需要刷新的用户列表（上次刷新日期不是今天的用户）
     */
    List<User> selectUsersNeedRefresh(@Param("todayStart") LocalDateTime todayStart);
    
    /**
     * 批量刷新用户每日抽奖次数
     */
    void batchRefreshDailyDraws(@Param("refreshTime") LocalDateTime refreshTime);
    
    /**
     * 获取所有用户
     */
    List<User> selectAll();
    
    /**
     * 获取所有有居住地点的用户
     */
    List<User> selectAllWithResidence();
    
    /**
     * 扣减用户耐力值
     */
    void decreaseStamina(@Param("userId") String userId);
    
    /**
     * 批量刷新所有用户耐力值到5
     */
    void batchRefreshStamina();
    
    /**
     * 获取用户精力信息
     */
    Integer getUserEnergy(@Param("userId") String userId);
    
    /**
     * 更新用户精力
     */
    void updateUserEnergy(@Param("userId") String userId, @Param("energy") Integer energy, @Param("energyRefreshTime") LocalDateTime energyRefreshTime);
    
    /**
     * 批量刷新所有用户的精力到满值
     */
    void batchRefreshEnergy();
    
    /**
     * 删除用户
     */
    void deleteByUserId(@Param("userId") String userId);
}
