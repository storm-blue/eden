package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 居所事件历史实体类
 */
public class ResidenceEventHistory {
    private Long id;
    private String residence;
    private String eventData;
    private String residentsInfo;
    private Boolean showHeartEffect;
    private String specialText;
    private Boolean showSpecialEffect;
    private LocalDateTime createdAt;

    // 默认构造函数
    public ResidenceEventHistory() {}

    // 构造函数
    public ResidenceEventHistory(String residence, String eventData, String residentsInfo) {
        this.residence = residence;
        this.eventData = eventData;
        this.residentsInfo = residentsInfo;
        this.showHeartEffect = false;
        this.showSpecialEffect = false;
        this.createdAt = LocalDateTime.now();
    }

    public ResidenceEventHistory(String residence, String eventData, String residentsInfo, 
                               Boolean showHeartEffect, String specialText, Boolean showSpecialEffect) {
        this.residence = residence;
        this.eventData = eventData;
        this.residentsInfo = residentsInfo;
        this.showHeartEffect = showHeartEffect;
        this.specialText = specialText;
        this.showSpecialEffect = showSpecialEffect;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public String getResidentsInfo() {
        return residentsInfo;
    }

    public void setResidentsInfo(String residentsInfo) {
        this.residentsInfo = residentsInfo;
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

    @Override
    public String toString() {
        return "ResidenceEventHistory{" +
                "id=" + id +
                ", residence='" + residence + '\'' +
                ", eventData='" + eventData + '\'' +
                ", residentsInfo='" + residentsInfo + '\'' +
                ", showHeartEffect=" + showHeartEffect +
                ", specialText='" + specialText + '\'' +
                ", showSpecialEffect=" + showSpecialEffect +
                ", createdAt=" + createdAt +
                '}';
    }
}
