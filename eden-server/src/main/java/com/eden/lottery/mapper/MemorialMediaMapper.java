package com.eden.lottery.mapper;

import com.eden.lottery.entity.MemorialMedia;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 纪念堂媒体文件Mapper
 */
@Mapper
public interface MemorialMediaMapper {

    /**
     * 插入媒体文件记录
     */
    @Insert("INSERT INTO memorial_media (file_name, original_name, file_path, file_type, mime_type, file_size, url, upload_time, create_time, update_time) " +
            "VALUES (#{fileName}, #{originalName}, #{filePath}, #{fileType}, #{mimeType}, #{fileSize}, #{url}, #{uploadTime}, #{createTime}, #{updateTime})")
    int insertMemorialMedia(MemorialMedia memorialMedia);

    /**
     * 根据ID查询媒体文件
     */
    @Select("SELECT * FROM memorial_media WHERE id = #{id}")
    MemorialMedia selectById(Long id);

    /**
     * 查询所有媒体文件（按上传时间倒序）
     */
    @Select("SELECT * FROM memorial_media ORDER BY upload_time DESC")
    List<MemorialMedia> selectAll();

    /**
     * 根据ID删除媒体文件
     */
    @Delete("DELETE FROM memorial_media WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 统计媒体文件数量
     */
    @Select("SELECT COUNT(*) FROM memorial_media")
    int countMemorialMedia();
}
