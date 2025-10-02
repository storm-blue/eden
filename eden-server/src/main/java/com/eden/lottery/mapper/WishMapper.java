package com.eden.lottery.mapper;

import com.eden.lottery.entity.Wish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 许愿Mapper接口
 */
@Mapper
public interface WishMapper {
    
    /**
     * 插入许愿
     * @param wish 许愿对象
     * @return 影响行数
     */
    int insert(Wish wish);
    
    /**
     * 根据ID查询许愿
     * @param id 许愿ID
     * @return 许愿对象
     */
    Wish selectById(@Param("id") Long id);
    
    /**
     * 查询所有许愿（按创建时间倒序）
     * @return 许愿列表
     */
    List<Wish> selectAll();
    
    /**
     * 根据用户ID查询许愿
     * @param userId 用户ID
     * @return 许愿列表
     */
    List<Wish> selectByUserId(@Param("userId") String userId);
    
    /**
     * 统计许愿总数
     * @return 总数
     */
    long count();
    
    /**
     * 统计用户许愿数
     * @param userId 用户ID
     * @return 用户许愿数
     */
    long countByUserId(@Param("userId") String userId);
    
    /**
     * 删除许愿
     * @param id 许愿ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 更新许愿内容
     * @param wish 许愿对象
     * @return 影响行数
     */
    int updateById(Wish wish);
}
