package com.eden.lottery.mapper;

import com.eden.lottery.entity.GiantAttack;
import org.apache.ibatis.annotations.*;

/**
 * 巨人进攻Mapper
 */
@Mapper
public interface GiantAttackMapper {

    /**
     * 获取当前巨人进攻状态
     */
    @Select("SELECT * FROM giant_attack ORDER BY create_time DESC LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "lastDamageTime", column = "last_damage_time"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    GiantAttack getCurrentGiantAttack();

    /**
     * 插入新的巨人进攻记录
     */
    @Insert("INSERT INTO giant_attack (is_active, start_time, end_time, last_damage_time, create_time, update_time) " +
            "VALUES (#{isActive}, #{startTime}, #{endTime}, #{lastDamageTime}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertGiantAttack(GiantAttack giantAttack);

    /**
     * 更新巨人进攻状态
     */
    @Update("UPDATE giant_attack SET is_active = #{isActive}, end_time = #{endTime}, " +
            "last_damage_time = #{lastDamageTime}, update_time = #{updateTime} WHERE id = #{id}")
    int updateGiantAttack(GiantAttack giantAttack);

    /**
     * 结束所有活跃的巨人进攻
     */
    @Update("UPDATE giant_attack SET is_active = 0, end_time = #{endTime}, update_time = #{updateTime} " +
            "WHERE is_active = 1")
    int endAllActiveAttacks(@Param("endTime") java.time.LocalDateTime endTime, 
                           @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 获取所有巨人进攻历史记录
     */
    @Select("SELECT * FROM giant_attack ORDER BY create_time DESC LIMIT 50")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "lastDamageTime", column = "last_damage_time"),
        @Result(property = "createTime", column = "create_time"),
        @Result(property = "updateTime", column = "update_time")
    })
    java.util.List<GiantAttack> getAllGiantAttackHistory();
}
