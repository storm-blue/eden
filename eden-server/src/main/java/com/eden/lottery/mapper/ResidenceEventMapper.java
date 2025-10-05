package com.eden.lottery.mapper;

import com.eden.lottery.entity.ResidenceEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 居所事件数据访问层
 */
@Mapper
public interface ResidenceEventMapper {
    
    /**
     * 根据居所类型查询最新的事件
     * @param residence 居所类型
     * @return 居所事件
     */
    ResidenceEvent selectByResidence(@Param("residence") String residence);
    
    /**
     * 插入新的居所事件
     * @param residenceEvent 居所事件
     * @return 影响行数
     */
    int insert(ResidenceEvent residenceEvent);
    
    /**
     * 更新居所事件
     * @param residenceEvent 居所事件
     * @return 影响行数
     */
    int update(ResidenceEvent residenceEvent);
    
    /**
     * 根据居所类型删除事件
     * @param residence 居所类型
     * @return 影响行数
     */
    int deleteByResidence(@Param("residence") String residence);
    
    /**
     * 插入或更新居所事件（UPSERT操作）
     * @param residenceEvent 居所事件
     * @return 影响行数
     */
    int upsert(ResidenceEvent residenceEvent);
}
