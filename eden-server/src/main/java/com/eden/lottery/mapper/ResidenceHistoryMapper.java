package com.eden.lottery.mapper;

import com.eden.lottery.entity.ResidenceHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 居住历史记录 Mapper
 */
@Mapper
public interface ResidenceHistoryMapper {
    
    /**
     * 插入居住历史记录
     * @param residenceHistory 居住历史记录
     * @return 受影响的行数
     */
    int insert(ResidenceHistory residenceHistory);
    
    /**
     * 根据用户ID查询居住历史
     * @param userId 用户ID
     * @return 居住历史列表
     */
    List<ResidenceHistory> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据居住地点查询历史记录
     * @param residence 居住地点
     * @return 居住历史列表
     */
    List<ResidenceHistory> selectByResidence(@Param("residence") String residence);
    
    /**
     * 查询所有居住历史记录（管理员查看）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 居住历史列表
     */
    List<ResidenceHistory> selectAll(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计总记录数
     * @return 总记录数
     */
    int countAll();
    
    /**
     * 根据用户ID统计记录数
     * @param userId 用户ID
     * @return 记录数
     */
    int countByUserId(@Param("userId") String userId);
    
    /**
     * 根据居住地点统计记录数
     * @param residence 居住地点
     * @return 记录数
     */
    int countByResidence(@Param("residence") String residence);
    
    /**
     * 获取用户的最新居住记录
     * @param userId 用户ID
     * @return 最新居住记录
     */
    ResidenceHistory selectLatestByUserId(@Param("userId") String userId);
    
    /**
     * 删除指定ID的记录（管理员功能）
     * @param id 记录ID
     * @return 受影响的行数
     */
    int deleteById(@Param("id") Long id);
}
