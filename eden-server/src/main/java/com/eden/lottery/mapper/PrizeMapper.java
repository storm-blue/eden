package com.eden.lottery.mapper;

import com.eden.lottery.entity.Prize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 奖品Mapper接口
 */
@Mapper
public interface PrizeMapper {
    
    /**
     * 插入奖品
     */
    int insert(Prize prize);
    
    /**
     * 插入或更新奖品（UPSERT）
     * 如果ID存在则更新，不存在则插入
     */
    int insertOrUpdate(Prize prize);
    
    /**
     * 根据ID查询奖品
     */
    Prize selectById(@Param("id") Long id);
    
    /**
     * 查询所有奖品
     */
    List<Prize> selectAll();
    
    /**
     * 查询有效奖品（概率大于0）
     */
    List<Prize> selectValidPrizes();
    
    /**
     * 根据级别查询奖品
     */
    List<Prize> selectByLevel(@Param("level") String level);
    
    /**
     * 更新奖品
     */
    int updateById(Prize prize);
    
    /**
     * 删除奖品
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 统计奖品数量
     */
    long count();
}
