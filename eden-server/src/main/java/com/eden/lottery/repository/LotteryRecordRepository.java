package com.eden.lottery.repository;

import com.eden.lottery.entity.LotteryRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 抽奖记录数据访问层
 */
@Repository
public interface LotteryRecordRepository extends JpaRepository<LotteryRecord, Long> {

    /**
     * 根据用户ID查询抽奖记录
     */
    List<LotteryRecord> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /**
     * 查询指定时间范围内的抽奖记录
     */
    @Query("SELECT r FROM LotteryRecord r WHERE r.createdAt BETWEEN :startTime AND :endTime ORDER BY r.createdAt DESC")
    List<LotteryRecord> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 统计用户在指定日期的抽奖次数
     */
    @Query("SELECT COUNT(r) FROM LotteryRecord r WHERE r.userId = :userId AND DATE(r.createdAt) = DATE(:date)")
    long countByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDateTime date);

    /**
     * 获取抽奖统计信息
     */
    @Query("SELECT p.name, p.level, COUNT(r) as count FROM LotteryRecord r JOIN r.prize p GROUP BY p.id, p.name, p.level")
    List<Object[]> getPrizeStatistics();

    /**
     * 查询最近的抽奖记录
     */
    List<LotteryRecord> findTop50ByOrderByCreatedAtDesc();
}
