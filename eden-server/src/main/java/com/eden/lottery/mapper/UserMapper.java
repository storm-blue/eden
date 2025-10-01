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
}
