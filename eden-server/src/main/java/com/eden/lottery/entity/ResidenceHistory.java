package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 居住历史记录实体类
 */
public class ResidenceHistory {
    
    private Long id;
    private String userId;
    private String residence;
    private String previousResidence;
    private LocalDateTime changeTime;
    private String ipAddress;
    private String userAgent;
    
    // 构造函数
    public ResidenceHistory() {}
    
    public ResidenceHistory(String userId, String residence, String previousResidence) {
        this.userId = userId;
        this.residence = residence;
        this.previousResidence = previousResidence;
        this.changeTime = LocalDateTime.now();
    }
    
    public ResidenceHistory(String userId, String residence, String previousResidence, String ipAddress, String userAgent) {
        this(userId, residence, previousResidence);
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
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
    
    public String getResidence() {
        return residence;
    }
    
    public void setResidence(String residence) {
        this.residence = residence;
    }
    
    public String getPreviousResidence() {
        return previousResidence;
    }
    
    public void setPreviousResidence(String previousResidence) {
        this.previousResidence = previousResidence;
    }
    
    public LocalDateTime getChangeTime() {
        return changeTime;
    }
    
    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
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
    
    @Override
    public String toString() {
        return "ResidenceHistory{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", residence='" + residence + '\'' +
                ", previousResidence='" + previousResidence + '\'' +
                ", changeTime=" + changeTime +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
