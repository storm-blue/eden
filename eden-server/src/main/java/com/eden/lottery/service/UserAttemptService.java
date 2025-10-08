package com.eden.lottery.service;

import com.eden.lottery.entity.UserAttempt;
import com.eden.lottery.mapper.UserAttemptMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户尝试记录服务
 */
@Service
public class UserAttemptService {
    
    @Resource
    private UserAttemptMapper userAttemptMapper;
    
    /**
     * 记录用户尝试
     * @param attemptUserId 尝试的用户ID
     * @param userExists 用户是否存在
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     */
    @Transactional
    public void recordAttempt(String attemptUserId, boolean userExists, String ipAddress, String userAgent) {
        UserAttempt attempt = new UserAttempt(attemptUserId, userExists, ipAddress, userAgent);
        userAttemptMapper.insert(attempt);
    }
    
    /**
     * 获取所有尝试记录
     * @param limit 限制条数
     * @return 尝试记录列表
     */
    public List<UserAttempt> getAllAttempts(Integer limit) {
        return userAttemptMapper.selectAll(limit);
    }
    
    /**
     * 根据用户ID获取尝试记录
     * @param attemptUserId 尝试的用户ID
     * @param limit 限制条数
     * @return 尝试记录列表
     */
    public List<UserAttempt> getAttemptsByUserId(String attemptUserId, Integer limit) {
        return userAttemptMapper.selectByUserId(attemptUserId, limit);
    }
    
    /**
     * 获取指定时间范围内的尝试记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 限制条数
     * @return 尝试记录列表
     */
    public List<UserAttempt> getAttemptsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Integer limit) {
        return userAttemptMapper.selectByTimeRange(startTime, endTime, limit);
    }
    
    /**
     * 获取最近的尝试记录
     * @param hours 最近多少小时
     * @param limit 限制条数
     * @return 尝试记录列表
     */
    public List<UserAttempt> getRecentAttempts(Integer hours, Integer limit) {
        return userAttemptMapper.selectRecentAttempts(hours, limit);
    }
    
    /**
     * 获取尝试统计信息
     * @return 统计信息
     */
    public Object getAttemptStatistics() {
        Long totalAttempts = userAttemptMapper.countTotal();
        Long nonExistentUserAttempts = userAttemptMapper.countNonExistentUsers();
        Long existentUserAttempts = totalAttempts - nonExistentUserAttempts;
        
        final Long finalTotalAttempts = totalAttempts;
        final Long finalExistentUserAttempts = existentUserAttempts;
        final Long finalNonExistentUserAttempts = nonExistentUserAttempts;
        
        return new Object() {
            public final Long totalAttempts = finalTotalAttempts;
            public final Long existentUserAttempts = finalExistentUserAttempts;
            public final Long nonExistentUserAttempts = finalNonExistentUserAttempts;
            public final Double nonExistentRate = finalTotalAttempts > 0 ? (double) finalNonExistentUserAttempts / finalTotalAttempts * 100 : 0.0;
        };
    }
    
    /**
     * 清理旧的尝试记录
     * @param beforeTime 指定时间之前的记录将被删除
     * @return 删除的记录数
     */
    @Transactional
    public int cleanOldAttempts(LocalDateTime beforeTime) {
        return userAttemptMapper.deleteBeforeTime(beforeTime);
    }
    
    /**
     * 清理30天前的记录
     * @return 删除的记录数
     */
    @Transactional
    public int cleanOldAttempts() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return cleanOldAttempts(thirtyDaysAgo);
    }
}
