package com.eden.lottery.mapper;

import com.eden.lottery.entity.ResidenceEventHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 居所事件历史Mapper接口
 */
@Mapper
public interface ResidenceEventHistoryMapper {

    /**
     * 插入事件历史记录
     */
    @Insert("INSERT INTO residence_event_history (residence, event_data, residents_info, show_heart_effect, special_text, show_special_effect) " +
            "VALUES (#{residence}, #{eventData}, #{residentsInfo}, #{showHeartEffect}, #{specialText}, #{showSpecialEffect})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertEventHistory(ResidenceEventHistory eventHistory);

    /**
     * 根据居所获取最近的事件历史（限制数量）
     */
    @Select("SELECT * FROM residence_event_history WHERE residence = #{residence} " +
            "ORDER BY created_at ASC LIMIT #{limit}")
    List<ResidenceEventHistory> getRecentEventHistory(@Param("residence") String residence, @Param("limit") int limit);

    /**
     * 获取指定居所的所有事件历史
     */
    @Select("SELECT * FROM residence_event_history WHERE residence = #{residence} ORDER BY created_at ASC")
    List<ResidenceEventHistory> getAllEventHistory(@Param("residence") String residence);

    /**
     * 根据ID删除事件历史
     */
    @Delete("DELETE FROM residence_event_history WHERE id = #{id}")
    int deleteEventHistory(@Param("id") Long id);

    /**
     * 删除指定居所的所有历史记录
     */
    @Delete("DELETE FROM residence_event_history WHERE residence = #{residence}")
    int deleteAllEventHistoryByResidence(@Param("residence") String residence);

    /**
     * 清理超过指定数量的旧记录（保留最新的N条）
     */
    @Delete("DELETE FROM residence_event_history WHERE residence = #{residence} AND id NOT IN " +
            "(SELECT id FROM (SELECT id FROM residence_event_history WHERE residence = #{residence} " +
            "ORDER BY created_at DESC LIMIT #{keepCount}) AS recent_records)")
    int cleanupOldEventHistory(@Param("residence") String residence, @Param("keepCount") int keepCount);

    /**
     * 获取指定居所的事件历史总数
     */
    @Select("SELECT COUNT(*) FROM residence_event_history WHERE residence = #{residence}")
    int getEventHistoryCount(@Param("residence") String residence);

    /**
     * 获取所有居所的事件历史总数
     */
    @Select("SELECT COUNT(*) FROM residence_event_history")
    int getTotalEventHistoryCount();
}
