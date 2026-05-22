package com.yomu.gamification.dto;

public class UnreadCountResponse {
    private Long count;

    public UnreadCountResponse() {}

    public UnreadCountResponse(Long count) {
        this.count = count;
    }

    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}