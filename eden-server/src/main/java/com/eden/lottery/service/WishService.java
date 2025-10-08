package com.eden.lottery.service;

import com.eden.lottery.entity.Wish;
import com.eden.lottery.mapper.WishMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * 许愿服务
 */
@Service
public class WishService {
    
    private static final Logger logger = LoggerFactory.getLogger(WishService.class);
    
    @Resource
    private WishMapper wishMapper;
    
    private final Random random = new Random();
    
    /**
     * 创建许愿
     * @param userId 用户ID
     * @param wishContent 许愿内容
     * @return 许愿对象
     */
    @Transactional
    public Wish createWish(String userId, String wishContent) {
        // 生成随机星星位置，避免重叠
        Double starX = generateRandomPosition();
        Double starY = generateRandomPosition();
        Integer starSize = generateRandomStarSize();
        
        Wish wish = new Wish(userId, wishContent, starX, starY, starSize);
        wishMapper.insert(wish);
        
        return wish;
    }
    
    /**
     * 获取所有许愿（用于显示星空）
     * @return 许愿列表
     */
    public List<Wish> getAllWishes() {
        return wishMapper.selectAll();
    }
    
    /**
     * 根据用户ID获取许愿
     * @param userId 用户ID
     * @return 用户的许愿列表
     */
    public List<Wish> getWishesByUserId(String userId) {
        return wishMapper.selectByUserId(userId);
    }
    
    /**
     * 根据ID获取许愿
     * @param id 许愿ID
     * @return 许愿对象
     */
    public Wish getWishById(Long id) {
        return wishMapper.selectById(id);
    }
    
    /**
     * 获取许愿统计信息
     * @return 统计信息
     */
    public Object getWishStatistics() {
        long totalWishes = wishMapper.count();
        
        return new Object() {
            public final long totalWishes = wishMapper.count();
            public final String message = "共有 " + totalWishes + " 个美好愿望在星空中闪耀";
        };
    }
    
    /**
     * 删除许愿（管理员功能）
     * @param id 许愿ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteWish(Long id) {
        try {
            int result = wishMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            logger.error("删除许愿失败", e);
            return false;
        }
    }
    
    /**
     * 获取所有许愿（管理员查看）
     * @return 许愿列表（包含完整信息）
     */
    public List<Wish> getAllWishesForAdmin() {
        return wishMapper.selectAll();
    }
    
    /**
     * 生成随机位置（避免边缘，确保星星完全显示）
     * @return 位置百分比 (10-90)
     */
    private Double generateRandomPosition() {
        return 10.0 + random.nextDouble() * 80.0; // 10% 到 90%
    }
    
    /**
     * 生成随机星星大小
     * @return 星星大小 (2-5)
     */
    private Integer generateRandomStarSize() {
        return 2 + random.nextInt(4); // 2, 3, 4, 5
    }
}
