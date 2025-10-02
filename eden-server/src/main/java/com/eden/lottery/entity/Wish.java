package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 许愿实体类
 */
public class Wish {
    
    private Long id;
    private String userId;
    private String wishContent;
    private Double starX;          // 星星X坐标 (0-100%)
    private Double starY;          // 星星Y坐标 (0-100%)
    private Integer starSize;      // 星星大小 (1-5)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    public Wish() {}
    
    public Wish(String userId, String wishContent, Double starX, Double starY, Integer starSize) {
        this.userId = userId;
        this.wishContent = wishContent;
        this.starX = starX;
        this.starY = starY;
        this.starSize = starSize;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Double getStarX() {
        return starX;
    }
    
    public void setStarX(Double starX) {
        this.starX = starX;
    }
    
    public Double getStarY() {
        return starY;
    }
    
    public void setStarY(Double starY) {
        this.starY = starY;
    }
    
    public Integer getStarSize() {
        return starSize;
    }
    
    public void setStarSize(Integer starSize) {
        this.starSize = starSize;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "Wish{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", wishContent='" + wishContent + '\'' +
                ", starX=" + starX +
                ", starY=" + starY +
                ", starSize=" + starSize +
                ", createTime=" + createTime +
                '}';
    }
}
