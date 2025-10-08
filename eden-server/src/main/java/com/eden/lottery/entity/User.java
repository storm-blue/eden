package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
public class User {
    
    /**
     * 用户ID（姓名）
     */
    private String userId;
    
    /**
     * 剩余抽奖次数
     */
    private Integer remainingDraws;
    
    /**
     * 每日刷新的抽奖次数
     */
    private Integer dailyDraws;
    
    /**
     * 可用许愿次数
     */
    private Integer wishCount;
    
    /**
     * 居住地点
     */
    private String residence;
    
    /**
     * 头像文件路径
     */
    private String avatarPath;
    
    /**
     * 用户简介
     */
    private String profile;
    
    /**
     * 用户状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 最后刷新日期（用于判断是否需要每日刷新）
     */
    private LocalDateTime lastRefreshDate;

    public User() {}

    public User(String userId, Integer dailyDraws) {
        this.userId = userId;
        this.dailyDraws = dailyDraws;
        this.remainingDraws = dailyDraws; // 初始剩余次数等于每日次数
        this.wishCount = 0; // 初始许愿次数为0
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.lastRefreshDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRemainingDraws() {
        return remainingDraws;
    }

    public void setRemainingDraws(Integer remainingDraws) {
        this.remainingDraws = remainingDraws;
    }

    public Integer getDailyDraws() {
        return dailyDraws;
    }

    public void setDailyDraws(Integer dailyDraws) {
        this.dailyDraws = dailyDraws;
    }

    public Integer getWishCount() {
        return wishCount;
    }

    public void setWishCount(Integer wishCount) {
        this.wishCount = wishCount;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getLastRefreshDate() {
        return lastRefreshDate;
    }

    public void setLastRefreshDate(LocalDateTime lastRefreshDate) {
        this.lastRefreshDate = lastRefreshDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", remainingDraws=" + remainingDraws +
                ", dailyDraws=" + dailyDraws +
                ", wishCount=" + wishCount +
                ", residence='" + residence + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", profile='" + profile + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", lastRefreshDate=" + lastRefreshDate +
                '}';
    }
}
