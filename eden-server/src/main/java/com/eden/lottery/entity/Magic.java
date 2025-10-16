package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 魔法实体 - 秦小淮施展的魔法
 */
public class Magic {
    
    /**
     * 魔法ID
     */
    private Long id;
    
    /**
     * 魔法代码：FOOD_RAIN（天降食物）
     */
    private String code;
    
    /**
     * 魔法名称
     */
    private String name;
    
    /**
     * 魔法描述
     */
    private String description;
    
    /**
     * 每日可施展次数
     */
    private Integer dailyLimit;
    
    /**
     * 当日剩余次数
     */
    private Integer remainingUses;
    
    /**
     * 上次刷新时间
     */
    private LocalDateTime lastRefreshAt;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    public Magic() {}
    
    public Magic(String code, String name, String description, Integer dailyLimit) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.dailyLimit = dailyLimit;
        this.remainingUses = dailyLimit;
        this.lastRefreshAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDailyLimit() {
        return dailyLimit;
    }
    
    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }
    
    public Integer getRemainingUses() {
        return remainingUses;
    }
    
    public void setRemainingUses(Integer remainingUses) {
        this.remainingUses = remainingUses;
    }
    
    public LocalDateTime getLastRefreshAt() {
        return lastRefreshAt;
    }
    
    public void setLastRefreshAt(LocalDateTime lastRefreshAt) {
        this.lastRefreshAt = lastRefreshAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Magic{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dailyLimit=" + dailyLimit +
                ", remainingUses=" + remainingUses +
                ", lastRefreshAt=" + lastRefreshAt +
                ", createdAt=" + createdAt +
                '}';
    }
}

