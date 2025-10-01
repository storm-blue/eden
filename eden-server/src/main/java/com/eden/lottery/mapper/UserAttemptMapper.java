package com.eden.lottery.mapper;

import com.eden.lottery.entity.UserAttempt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户尝试记录Mapper接口
 */
@Mapper
public interface UserAttemptMapper {
    
    /**
     * 插入用户尝试记录
     * @param userAttempt 用户尝试记录
     * @return 影响行数
     */
    int insert(UserAttempt userAttempt);
    
    /**
     * 查询所有用户尝试记录（按时间倒序）
     * @param limit 限制条数
     * @return 用户尝试记录列表
     */
    List<UserAttempt> selectAll(@Param("limit") Integer limit);
    
    /**
     * 根据用户ID查询尝试记录
     * @param attemptUserId 尝试的用户ID
     * @param limit 限制条数
     * @return 用户尝试记录列表
     */
    List<UserAttempt> selectByUserId(@Param("attemptUserId") String attemptUserId, @Param("limit") Integer limit);
    
    /**
     * 查询指定时间范围内的尝试记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制条数
     * @return 用户尝试记录列表
     */
    List<UserAttempt> selectByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime, 
                                       @Param("limit") Integer limit);
    
    /**
     * 统计总尝试次数
     * @return 总次数
     */
    Long countTotal();
    
    /**
     * 统计不存在用户的尝试次数
     * @return 不存在用户的尝试次数
     */
    Long countNonExistentUsers();
    
    /**
     * 获取最近的尝试记录统计
     * @param hours 最近多少小时
     * @return 统计结果
     */
    List<UserAttempt> selectRecentAttempts(@Param("hours") Integer hours, @Param("limit") Integer limit);
    
    /**
     * 删除指定时间之前的记录（清理旧数据）
     * @param beforeTime 指定时间
     * @return 删除的记录数
     */
    int deleteBeforeTime(@Param("beforeTime") LocalDateTime beforeTime);
}
