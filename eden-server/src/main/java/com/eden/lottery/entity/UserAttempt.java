package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 用户尝试记录实体类
 * 记录所有用户输入的用户名，包括存在和不存在的用户
 */
public class UserAttempt {
    
    private Long id;
    private String attemptUserId;  // 尝试输入的用户ID
    private Boolean userExists;    // 用户是否存在
    private String ipAddress;      // 访问IP地址
    private String userAgent;      // 用户代理信息
    private LocalDateTime attemptTime; // 尝试时间
    
    // 构造函数
    public UserAttempt() {}
    
    public UserAttempt(String attemptUserId, Boolean userExists, String ipAddress, String userAgent) {
        this.attemptUserId = attemptUserId;
        this.userExists = userExists;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.attemptTime = LocalDateTime.now();
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAttemptUserId() {
        return attemptUserId;
    }
    
    public void setAttemptUserId(String attemptUserId) {
        this.attemptUserId = attemptUserId;
    }
    
    public Boolean getUserExists() {
        return userExists;
    }
    
    public void setUserExists(Boolean userExists) {
        this.userExists = userExists;
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
    
    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }
    
    public void setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }
    
    @Override
    public String toString() {
        return "UserAttempt{" +
                "id=" + id +
                ", attemptUserId='" + attemptUserId + '\'' +
                ", userExists=" + userExists +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", attemptTime=" + attemptTime +
                '}';
    }
}
