package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 抽奖记录实体类
 */
public class LotteryRecord {
    
    private Long id;
    private String userId;
    private Long prizeId;
    private Prize prize; // 关联的奖品对象，用于查询时填充
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
    
    public LotteryRecord() {}
    
    public LotteryRecord(String userId, Long prizeId, String ipAddress, String userAgent) {
        this.userId = userId;
        this.prizeId = prizeId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = LocalDateTime.now();
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
    
    public Long getPrizeId() {
        return prizeId;
    }
    
    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }
    
    public Prize getPrize() {
        return prize;
    }
    
    public void setPrize(Prize prize) {
        this.prize = prize;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "LotteryRecord{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", prizeId=" + prizeId +
                ", ipAddress='" + ipAddress + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}