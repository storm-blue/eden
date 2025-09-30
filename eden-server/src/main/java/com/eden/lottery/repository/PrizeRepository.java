package com.eden.lottery.repository;

import com.eden.lottery.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 奖品数据访问层
 */
@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
    
    /**
     * 按概率排序获取所有奖品
     */
    @Query("SELECT p FROM Prize p ORDER BY p.probability DESC")
    List<Prize> findAllOrderByProbabilityDesc();
    
    /**
     * 根据级别查询奖品
     */
    List<Prize> findByLevel(String level);
    
    /**
     * 查询概率大于0的有效奖品
     */
    @Query("SELECT p FROM Prize p WHERE p.probability > 0 ORDER BY p.id")
    List<Prize> findValidPrizes();
}
