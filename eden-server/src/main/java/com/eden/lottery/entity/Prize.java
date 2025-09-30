package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 奖品实体类
 */
public class Prize {
    
    private Long id;
    private String name;
    private Double probability;
    private String level;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Prize() {}
    
    public Prize(String name, Double probability, String level) {
        this.name = name;
        this.probability = probability;
        this.level = level;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getProbability() {
        return probability;
    }
    
    public void setProbability(Double probability) {
        this.probability = probability;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
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
        return "Prize{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", probability=" + probability +
                ", level='" + level + '\'' +
                '}';
    }
}
