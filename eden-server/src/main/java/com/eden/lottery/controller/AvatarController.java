package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.mapper.UserMapper;
import com.eden.lottery.entity.User;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户头像控制器
 */
@RestController
@RequestMapping("/api/avatar")
@CrossOrigin(origins = "*")
public class AvatarController {

    private static final Logger logger = LoggerFactory.getLogger(AvatarController.class);

    @Resource
    private UserMapper userMapper;

    // 头像存储目录
    @Value("${avatar.upload.path:./uploads/avatars/}")
    private String avatarUploadPath;

    // 头像访问URL前缀
    @Value("${avatar.url.prefix:/uploads/avatars/}")
    private String avatarUrlPrefix;

    /**
     * 上传并裁剪用户头像
     */
    @PostMapping("/upload")
    public ApiResponse<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam(value = "x", defaultValue = "0") int x,
            @RequestParam(value = "y", defaultValue = "0") int y,
            @RequestParam(value = "width", defaultValue = "200") int width,
            @RequestParam(value = "height", defaultValue = "200") int height) {
        
        try {
            // 验证用户是否存在
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            // 验证文件
            if (file.isEmpty()) {
                return ApiResponse.error("请选择要上传的文件");
            }

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ApiResponse.error("请上传图片文件");
            }

            // 验证文件大小（限制为5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return ApiResponse.error("文件大小不能超过5MB");
            }

            // 创建上传目录
            File uploadDir = new File(avatarUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 读取原始图片
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                return ApiResponse.error("无法读取图片文件");
            }

            logger.info("原始图片尺寸: {}x{}", originalImage.getWidth(), originalImage.getHeight());
            logger.info("裁剪参数: x={}, y={}, width={}, height={}", x, y, width, height);

            // 验证裁剪参数
            if (x < 0 || y < 0 || width <= 0 || height <= 0 ||
                x + width > originalImage.getWidth() || y + height > originalImage.getHeight()) {
                logger.error("裁剪参数无效: 图片尺寸 {}x{}, 裁剪区域 ({},{}) {}x{}", 
                    originalImage.getWidth(), originalImage.getHeight(), x, y, width, height);
                return ApiResponse.error("裁剪参数无效");
            }

            // 裁剪图片
            BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);

            // 缩放到200x200
            BufferedImage resizedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(croppedImage, 0, 0, 200, 200, null);
            g2d.dispose();

            // 生成文件名（使用时间戳避免中文字符问题）
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = "avatar_" + timestamp + "_" + UUID.randomUUID().toString().substring(0, 8) + ".jpg";
            File targetFile = new File(uploadDir, fileName);

            // 保存文件
            ImageIO.write(resizedImage, "jpg", targetFile);

            // 删除旧头像文件
            if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
                try {
                    String oldFileName = user.getAvatarPath().substring(user.getAvatarPath().lastIndexOf('/') + 1);
                    File oldFile = new File(uploadDir, oldFileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                } catch (Exception e) {
                    logger.warn("删除旧头像文件失败: {}", e.getMessage());
                }
            }

            // 更新数据库
            String avatarPath = avatarUrlPrefix + fileName;
            userMapper.updateAvatar(userId, avatarPath);

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("avatarPath", avatarPath); // 只返回相对路径，由前端拼接完整地址
            result.put("message", "头像上传成功");

            logger.info("用户 {} 头像上传成功: {}", userId, avatarPath);
            return ApiResponse.success("头像上传成功", result);

        } catch (IOException e) {
            logger.error("头像上传失败: {}", e.getMessage(), e);
            return ApiResponse.error("头像上传失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("头像上传失败: {}", e.getMessage(), e);
            return ApiResponse.error("头像上传失败");
        }
    }

    /**
     * 获取用户头像信息
     */
    @GetMapping("/{userId}")
    public ApiResponse<Map<String, Object>> getUserAvatar(@PathVariable String userId) {
        try {
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            
            // 只返回相对路径，由前端拼接完整地址
            String avatarPath = user.getAvatarPath();
            if (avatarPath != null && !avatarPath.isEmpty()) {
                result.put("avatarPath", avatarPath); // 返回相对路径
                result.put("hasAvatar", true);
            } else {
                result.put("avatarPath", null);
                result.put("hasAvatar", false);
            }

            return ApiResponse.success("获取头像信息成功", result);
        } catch (Exception e) {
            logger.error("获取用户头像信息失败: {}", e.getMessage(), e);
            return ApiResponse.error("获取头像信息失败");
        }
    }

    /**
     * 删除用户头像
     */
    @DeleteMapping("/{userId}")
    public ApiResponse<Object> deleteUserAvatar(@PathVariable String userId) {
        try {
            User user = userMapper.selectByUserId(userId);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }

            // 删除头像文件
            if (user.getAvatarPath() != null && !user.getAvatarPath().isEmpty()) {
                try {
                    String fileName = user.getAvatarPath().substring(user.getAvatarPath().lastIndexOf('/') + 1);
                    File avatarFile = new File(avatarUploadPath, fileName);
                    if (avatarFile.exists()) {
                        avatarFile.delete();
                    }
                } catch (Exception e) {
                    logger.warn("删除头像文件失败: {}", e.getMessage());
                }
            }

            // 更新数据库
            userMapper.updateAvatar(userId, null);

            logger.info("用户 {} 头像删除成功", userId);
            return ApiResponse.success("头像删除成功", null);

        } catch (Exception e) {
            logger.error("删除用户头像失败: {}", e.getMessage(), e);
            return ApiResponse.error("删除头像失败");
        }
    }
}
