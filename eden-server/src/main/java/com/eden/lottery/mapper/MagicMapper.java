package com.eden.lottery.mapper;

import com.eden.lottery.entity.Magic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 魔法数据访问接口
 */
@Mapper
public interface MagicMapper {
    
    /**
     * 插入魔法
     */
    void insert(Magic magic);
    
    /**
     * 根据代码查询魔法
     */
    Magic selectByCode(@Param("code") String code);
    
    /**
     * 更新魔法
     */
    void update(Magic magic);
    
    /**
     * 查询所有魔法
     */
    List<Magic> selectAll();
    
    /**
     * 减少魔法剩余次数
     */
    void decreaseRemainingUses(@Param("code") String code);
    
    /**
     * 刷新魔法次数
     */
    void refreshDailyUses(@Param("code") String code);
}

