package com.eden.lottery.dto;

import com.eden.lottery.entity.Prize;

import java.time.LocalDateTime;

/**
 * 抽奖结果DTO
 */
public class LotteryResult {
    
    private PrizeDto prize;
    private Long recordId;
    private LocalDateTime timestamp;
    
    public LotteryResult() {}
    
    public LotteryResult(Prize prize, Long recordId, LocalDateTime timestamp) {
        this.prize = new PrizeDto(prize.getId(), prize.getName(), prize.getLevel());
        this.recordId = recordId;
        this.timestamp = timestamp;
    }
    
    public PrizeDto getPrize() {
        return prize;
    }
    
    public void setPrize(PrizeDto prize) {
        this.prize = prize;
    }
    
    public Long getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * 奖品DTO（不包含概率信息）
     */
    public static class PrizeDto {
        private Long id;
        private String name;
        private String level;
        
        public PrizeDto() {}
        
        public PrizeDto(Long id, String name, String level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }
        
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
        
        public String getLevel() {
            return level;
        }
        
        public void setLevel(String level) {
            this.level = level;
        }
    }
}
