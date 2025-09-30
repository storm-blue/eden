package com.eden.lottery.dto;

/**
 * 抽奖请求DTO
 */
public class LotteryRequest {
    
    private String userId;
    
    public LotteryRequest() {}
    
    public LotteryRequest(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "LotteryRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}
