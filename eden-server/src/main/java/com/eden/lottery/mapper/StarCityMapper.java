package com.eden.lottery.mapper;

import com.eden.lottery.entity.StarCity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 星星城数据访问层
 */
@Mapper
public interface StarCityMapper {
    
    /**
     * 获取星星城数据（只有一条记录）
     */
    StarCity getStarCity();
    
    /**
     * 插入星星城数据
     */
    int insertStarCity(StarCity starCity);
    
    /**
     * 更新星星城数据
     */
    int updateStarCity(StarCity starCity);
    
    /**
     * 每日更新星星城数据
     */
    int dailyUpdate();
}
