package com.eden.lottery.entity;

import java.time.LocalDateTime;

/**
 * 星星城实体类
 */
public class StarCity {
    private Long id;
    private Long population;      // 人口数量
    private Long food;           // 食物数量
    private Integer happiness;   // 幸福指数
    private Integer level;       // 当前等级
    private LocalDateTime lastUpdateTime; // 最后更新时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造函数
    public StarCity() {}

    public StarCity(Long population, Long food, Integer happiness) {
        this.population = population;
        this.food = food;
        this.happiness = happiness;
        this.level = calculateLevel();
        this.lastUpdateTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 计算等级的方法
    public Integer calculateLevel() {
        if (population >= 1000000 && food >= 1000000 && happiness >= 100) {
            return 5;
        } else if (population >= 700000 && food >= 700000 && happiness >= 80) {
            return 4;
        } else if (population >= 400000 && food >= 400000 && happiness >= 50) {
            return 3;
        } else if (population >= 200000 && food >= 200000 && happiness >= 30) {
            return 2;
        } else {
            return 1;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Long getFood() {
        return food;
    }

    public void setFood(Long food) {
        this.food = food;
    }

    public Integer getHappiness() {
        return happiness;
    }

    public void setHappiness(Integer happiness) {
        this.happiness = happiness;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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
        return "StarCity{" +
                "id=" + id +
                ", population=" + population +
                ", food=" + food +
                ", happiness=" + happiness +
                ", level=" + level +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
