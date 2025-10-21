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
    private String weather;      // 天气状态: sunny, rainy, snowy, cloudy, night
    private Boolean isRuins;     // 废墟状态: true=废墟状态, false=正常状态
    private LocalDateTime lastUpdateTime; // 最后更新时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 构造函数
    public StarCity() {}

    public StarCity(Long population, Long food, Integer happiness) {
        this.population = population;
        this.food = food;
        this.happiness = happiness;
        this.isRuins = false; // 默认非废墟状态
        this.level = calculateLevel();
        this.lastUpdateTime = LocalDateTime.now();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 计算等级的方法
    public Integer calculateLevel() {
        // 废墟状态下强制等级为0
        if (isRuins != null && isRuins) {
            return 0;
        }
        
        if (population >= 20000000 && food >= 20000000 && happiness >= 2000) {
            return 10;
        } else if (population >= 10000000 && food >= 10000000 && happiness >= 1000) {
            return 9;
        } else if (population >= 5000000 && food >= 5000000 && happiness >= 500) {
            return 8;
        } else if (population >= 3000000 && food >= 3000000 && happiness >= 300) {
            return 7;
        } else if (population >= 1500000 && food >= 1500000 && happiness >= 150) {
            return 6;
        } else if (population >= 1000000 && food >= 1000000 && happiness >= 100) {
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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Boolean getIsRuins() {
        return isRuins;
    }

    public void setIsRuins(Boolean isRuins) {
        this.isRuins = isRuins;
        // 当设置废墟状态时，重新计算等级
        if (isRuins != null) {
            this.level = calculateLevel();
        }
    }

    @Override
    public String toString() {
        return "StarCity{" +
                "id=" + id +
                ", population=" + population +
                ", food=" + food +
                ", happiness=" + happiness +
                ", level=" + level +
                ", weather='" + weather + '\'' +
                ", isRuins=" + isRuins +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
