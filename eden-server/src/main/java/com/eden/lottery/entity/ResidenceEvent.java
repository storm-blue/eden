package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 居所事件实体
 */
public class ResidenceEvent {
    
    private Long id;
    private String residence; // 居所类型：castle, city_hall, palace, dove_house, park
    private String eventData; // 事件数据（JSON格式，包含多条事件描述和对应颜色）
    private Boolean showHeartEffect; // 是否显示爱心特效
    private String specialText; // 特殊文字（如情侣组合文字）
    private Boolean showSpecialEffect; // 是否显示特殊特效（爱心飞舞和粉红色特效）
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    
    // 构造函数
    public ResidenceEvent() {}
    
    public ResidenceEvent(String residence, String eventData, Boolean showHeartEffect) {
        this.residence = residence;
        this.eventData = eventData;
        this.showHeartEffect = showHeartEffect;
        this.specialText = null;
        this.showSpecialEffect = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public ResidenceEvent(String residence, String eventData, Boolean showHeartEffect, String specialText, Boolean showSpecialEffect) {
        this.residence = residence;
        this.eventData = eventData;
        this.showHeartEffect = showHeartEffect;
        this.specialText = specialText;
        this.showSpecialEffect = showSpecialEffect;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getResidence() {
        return residence;
    }
    
    public void setResidence(String residence) {
        this.residence = residence;
    }
    
    public String getEventData() {
        return eventData;
    }
    
    public void setEventData(String eventData) {
        this.eventData = eventData;
    }
    
    public Boolean getShowHeartEffect() {
        return showHeartEffect;
    }
    
    public void setShowHeartEffect(Boolean showHeartEffect) {
        this.showHeartEffect = showHeartEffect;
    }
    
    public String getSpecialText() {
        return specialText;
    }
    
    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }
    
    public Boolean getShowSpecialEffect() {
        return showSpecialEffect;
    }
    
    public void setShowSpecialEffect(Boolean showSpecialEffect) {
        this.showSpecialEffect = showSpecialEffect;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "ResidenceEvent{" +
                "id=" + id +
                ", residence='" + residence + '\'' +
                ", eventData='" + eventData + '\'' +
                ", showHeartEffect=" + showHeartEffect +
                ", specialText='" + specialText + '\'' +
                ", showSpecialEffect=" + showSpecialEffect +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
