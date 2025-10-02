package com.eden.lottery.controller;

import com.eden.lottery.dto.ApiResponse;
import com.eden.lottery.entity.Wish;
import com.eden.lottery.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 许愿API控制器
 */
@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private static final Logger logger = LoggerFactory.getLogger(WishController.class);

    @Autowired
    private WishService wishService;

    /**
     * 获取所有许愿（星空显示）
     */
    @GetMapping
    public ApiResponse<List<Object>> getAllWishes() {
        try {
            List<Wish> wishes = wishService.getAllWishes();
            
            List<Object> wishList = wishes.stream()
                    .map(wish -> new Object() {
                        public final Long id = wish.getId();
                        public final String userId = wish.getUserId();
                        public final String wishContent = wish.getWishContent();
                        public final Double starX = wish.getStarX();
                        public final Double starY = wish.getStarY();
                        public final Integer starSize = wish.getStarSize();
                        public final String createTime = wish.getCreateTime().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取许愿列表成功", wishList);
        } catch (Exception e) {
            logger.error("获取许愿列表失败", e);
            return ApiResponse.error("获取许愿列表失败");
        }
    }

    /**
     * 创建许愿
     */
    @PostMapping
    public ApiResponse<Object> createWish(@RequestBody CreateWishRequest request) {
        try {
            if (!StringUtils.hasText(request.getUserId())) {
                return ApiResponse.error("用户ID不能为空");
            }
            
            if (!StringUtils.hasText(request.getWishContent())) {
                return ApiResponse.error("许愿内容不能为空");
            }
            
            if (request.getWishContent().length() > 30) {
                return ApiResponse.error("许愿内容不能超过30个字符");
            }

            Wish wish = wishService.createWish(request.getUserId(), request.getWishContent());
            
            Object result = new Object() {
                public final Long id = wish.getId();
                public final String userId = wish.getUserId();
                public final String wishContent = wish.getWishContent();
                public final Double starX = wish.getStarX();
                public final Double starY = wish.getStarY();
                public final Integer starSize = wish.getStarSize();
                public final String createTime = wish.getCreateTime().toString();
                public final String message = "✨ 你的愿望已化作星光，在夜空中闪耀！";
            };

            logger.info("用户 {} 创建了新的许愿: {}", request.getUserId(), request.getWishContent());
            return ApiResponse.success("许愿成功", result);
        } catch (Exception e) {
            logger.error("创建许愿失败", e);
            return ApiResponse.error("许愿失败，请稍后重试");
        }
    }

    /**
     * 根据ID获取许愿详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Object> getWishById(@PathVariable Long id) {
        try {
            Wish wish = wishService.getWishById(id);
            if (wish == null) {
                return ApiResponse.error("许愿不存在");
            }

            Object result = new Object() {
                public final Long id = wish.getId();
                public final String userId = wish.getUserId();
                public final String wishContent = wish.getWishContent();
                public final Double starX = wish.getStarX();
                public final Double starY = wish.getStarY();
                public final Integer starSize = wish.getStarSize();
                public final String createTime = wish.getCreateTime().toString();
            };

            return ApiResponse.success("获取许愿详情成功", result);
        } catch (Exception e) {
            logger.error("获取许愿详情失败", e);
            return ApiResponse.error("获取许愿详情失败");
        }
    }

    /**
     * 获取用户的许愿列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Object>> getUserWishes(@PathVariable String userId) {
        try {
            List<Wish> wishes = wishService.getWishesByUserId(userId);
            
            List<Object> wishList = wishes.stream()
                    .map(wish -> new Object() {
                        public final Long id = wish.getId();
                        public final String wishContent = wish.getWishContent();
                        public final String createTime = wish.getCreateTime().toString();
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("获取用户许愿列表成功", wishList);
        } catch (Exception e) {
            logger.error("获取用户许愿列表失败", e);
            return ApiResponse.error("获取用户许愿列表失败");
        }
    }

    /**
     * 获取许愿统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<Object> getWishStatistics() {
        try {
            Object stats = wishService.getWishStatistics();
            return ApiResponse.success("获取许愿统计成功", stats);
        } catch (Exception e) {
            logger.error("获取许愿统计失败", e);
            return ApiResponse.error("获取许愿统计失败");
        }
    }

    /**
     * 创建许愿请求DTO
     */
    public static class CreateWishRequest {
        private String userId;
        private String wishContent;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWishContent() {
            return wishContent;
        }

        public void setWishContent(String wishContent) {
            this.wishContent = wishContent;
        }
    }
}
