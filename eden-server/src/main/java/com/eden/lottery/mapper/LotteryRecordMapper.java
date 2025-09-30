package com.eden.lottery.mapper;

import com.eden.lottery.entity.LotteryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 抽奖记录Mapper接口
 */
@Mapper
public interface LotteryRecordMapper {
    
    /**
     * 插入抽奖记录
     */
    int insert(LotteryRecord record);
    
    /**
     * 根据ID查询抽奖记录
     */
    LotteryRecord selectById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询抽奖记录
     */
    List<LotteryRecord> selectByUserId(@Param("userId") String userId, 
                                       @Param("offset") int offset, 
                                       @Param("limit") int limit);
    
    /**
     * 查询最近的抽奖记录
     */
    List<LotteryRecord> selectRecentRecords(@Param("limit") int limit);
    
    /**
     * 根据时间范围查询抽奖记录
     */
    List<LotteryRecord> selectByDateRange(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计用户在指定日期的抽奖次数
     */
    long countByUserIdAndDate(@Param("userId") String userId, 
                              @Param("date") String date);
    
    /**
     * 获取奖品统计信息
     */
    List<Map<String, Object>> selectPrizeStatistics();
    
    /**
     * 统计总抽奖次数
     */
    long count();
}
