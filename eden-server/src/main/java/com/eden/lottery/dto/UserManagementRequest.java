package com.eden.lottery.dto;

/**
 * 用户管理请求
 */
public class UserManagementRequest {
    private String userId;
    private Integer remainingDraws;
    private Integer dailyDraws;

    public UserManagementRequest() {}

    public UserManagementRequest(String userId, Integer remainingDraws, Integer dailyDraws) {
        this.userId = userId;
        this.remainingDraws = remainingDraws;
        this.dailyDraws = dailyDraws;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRemainingDraws() {
        return remainingDraws;
    }

    public void setRemainingDraws(Integer remainingDraws) {
        this.remainingDraws = remainingDraws;
    }

    public Integer getDailyDraws() {
        return dailyDraws;
    }

    public void setDailyDraws(Integer dailyDraws) {
        this.dailyDraws = dailyDraws;
    }

    @Override
    public String toString() {
        return "UserManagementRequest{" +
                "userId='" + userId + '\'' +
                ", remainingDraws=" + remainingDraws +
                ", dailyDraws=" + dailyDraws +
                '}';
    }
}
