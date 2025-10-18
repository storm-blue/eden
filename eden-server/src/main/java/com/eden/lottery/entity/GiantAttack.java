package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 巨人进攻实体类
 */
public class GiantAttack {
    private Long id;
    private Boolean isActive;           // 是否正在进攻
    private LocalDateTime startTime;    // 进攻开始时间
    private LocalDateTime endTime;      // 进攻结束时间
    private LocalDateTime lastDamageTime; // 上次造成伤害的时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造函数
    public GiantAttack() {}

    public GiantAttack(Boolean isActive) {
        this.isActive = isActive;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getLastDamageTime() {
        return lastDamageTime;
    }

    public void setLastDamageTime(LocalDateTime lastDamageTime) {
        this.lastDamageTime = lastDamageTime;
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
}
