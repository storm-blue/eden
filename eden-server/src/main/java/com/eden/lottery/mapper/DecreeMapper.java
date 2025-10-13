package com.eden.lottery.mapper;

import com.eden.lottery.entity.Decree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 命令数据访问接口
 */
@Mapper
public interface DecreeMapper {
    
    /**
     * 插入命令
     */
    void insert(Decree decree);
    
    /**
     * 根据代码查询命令
     */
    Decree selectByCode(@Param("code") String code);
    
    /**
     * 更新命令
     */
    void update(Decree decree);
    
    /**
     * 更新命令信息（仅名称和描述）
     */
    void updateInfo(Decree decree);
    
    /**
     * 查询所有命令
     */
    List<Decree> selectAll();
    
    /**
     * 查询所有已激活的命令
     */
    List<Decree> selectActive();
    
    /**
     * 检查命令是否激活
     */
    boolean isActive(@Param("code") String code);
}

