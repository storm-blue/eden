package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.entity.MemorialMedia;
import com.eden.lottery.mapper.MemorialMediaMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 纪念堂媒体文件控制器
 */
@RestController
@RequestMapping("/api/memorial")
@CrossOrigin(origins = "*")
public class MemorialController {

    private static final Logger logger = LoggerFactory.getLogger(MemorialController.class);

    @Resource
    private MemorialMediaMapper memorialMediaMapper;

    // 纪念堂文件存储目录
    @Value("${memorial.upload.path:./uploads/memorial/}")
    private String memorialUploadPath;

    // 纪念堂文件访问URL前缀
    @Value("${memorial.url.prefix:/uploads/memorial/}")
    private String memorialUrlPrefix;

    /**
     * 上传纪念堂媒体文件
     */
    @PostMapping("/upload")
    public ApiResponse<Map<String, Object>> uploadMemorialFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                return ApiResponse.error("请选择要上传的文件");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
                return ApiResponse.error("只支持图片和视频文件");
            }

            // 验证文件大小（限制为10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return ApiResponse.error("文件大小不能超过10MB");
            }

            // 创建上传目录 - 完全仿照头像上传
            File uploadDir = new File(memorialUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "memorial_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + fileExtension;
            File targetFile = new File(uploadDir, fileName);

            // 保存文件 - 仿照头像上传的方式
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(file.getBytes());
            }

            // 保存到数据库 - 仿照头像上传的方式
            String url = memorialUrlPrefix + fileName;
            
            MemorialMedia memorialMedia = new MemorialMedia(
                fileName,
                originalFileName,
                url, // 使用相对路径存储，与头像上传保持一致
                type,
                contentType,
                file.getSize(),
                url
            );

            int result = memorialMediaMapper.insertMemorialMedia(memorialMedia);
            if (result > 0) {
                // 由于SQLite不支持useGeneratedKeys，我们需要通过文件名查询获取ID
                List<MemorialMedia> mediaList = memorialMediaMapper.selectAll();
                MemorialMedia savedMedia = mediaList.stream()
                    .filter(media -> fileName.equals(media.getFileName()))
                    .findFirst()
                    .orElse(null);
                
                if (savedMedia != null) {
                    Map<String, Object> responseData = new HashMap<>();
                    responseData.put("id", savedMedia.getId());
                    responseData.put("fileName", fileName);
                    responseData.put("originalName", originalFileName);
                    responseData.put("filePath", targetFile.getAbsolutePath());
                    responseData.put("url", url);
                    responseData.put("type", type);
                    responseData.put("fileSize", file.getSize());
                    responseData.put("uploadTime", savedMedia.getUploadTime());

                    logger.info("纪念堂文件上传成功: {}", fileName);
                    return ApiResponse.success("文件上传成功", responseData);
                } else {
                    // 如果查询不到记录，删除已上传的文件
                    if (targetFile.exists()) {
                        targetFile.delete();
                    }
                    return ApiResponse.error("文件保存失败");
                }
            } else {
                // 如果数据库保存失败，删除已上传的文件
                if (targetFile.exists()) {
                    targetFile.delete();
                }
                return ApiResponse.error("文件保存失败");
            }

        } catch (IOException e) {
            logger.error("纪念堂文件上传失败: {}", e.getMessage(), e);
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("纪念堂文件上传失败: {}", e.getMessage(), e);
            return ApiResponse.error("文件上传失败");
        }
    }

    /**
     * 获取纪念堂媒体文件列表
     */
    @GetMapping("/list")
    public ApiResponse<List<MemorialMedia>> getMemorialMediaList() {
        try {
            List<MemorialMedia> mediaList = memorialMediaMapper.selectAll();
            logger.info("获取纪念堂媒体文件列表成功，共{}个文件", mediaList.size());
            return ApiResponse.success("获取文件列表成功", mediaList);
        } catch (Exception e) {
            logger.error("获取纪念堂媒体文件列表失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取文件列表失败");
        }
    }

    /**
     * 删除纪念堂媒体文件
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteMemorialMedia(@PathVariable Long id) {
        try {
            // 先查询文件信息
            MemorialMedia media = memorialMediaMapper.selectById(id);
            if (media == null) {
                return ApiResponse.error("文件不存在");
            }

            // 删除文件系统中的文件 - 将相对路径转换为绝对路径
            String fileName = media.getFileName();
            File targetFile = new File(memorialUploadPath + fileName);
            if (targetFile.exists()) {
                boolean deleted = targetFile.delete();
                if (deleted) {
                    logger.info("文件系统文件删除成功: {}", targetFile.getAbsolutePath());
                } else {
                    logger.warn("文件系统文件删除失败: {}", targetFile.getAbsolutePath());
                }
            }

            // 删除数据库记录
            int result = memorialMediaMapper.deleteById(id);
            if (result > 0) {
                logger.info("纪念堂文件删除成功: {}", media.getFileName());
                return ApiResponse.success("文件删除成功", null);
            } else {
                return ApiResponse.error("文件删除失败");
            }

        } catch (Exception e) {
            logger.error("删除纪念堂文件失败: {}", e.getMessage(), e);
            return ApiResponse.error("文件删除失败");
        }
    }

    /**
     * 获取纪念堂统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getMemorialStats() {
        try {
            int totalCount = memorialMediaMapper.countMemorialMedia();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCount", totalCount);
            stats.put("message", "纪念堂统计信息");
            
            logger.info("获取纪念堂统计信息成功，共{}个文件", totalCount);
            return ApiResponse.success("获取统计信息成功", stats);
        } catch (Exception e) {
            logger.error("获取纪念堂统计信息失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取统计信息失败");
        }
    }
}
