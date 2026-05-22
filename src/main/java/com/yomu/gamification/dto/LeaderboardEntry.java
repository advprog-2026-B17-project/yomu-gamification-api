package com.yomu.gamification.dto;

public class LeaderboardEntry {
    private String userId;
    private String displayName;
    private String username;
    private Long totalScore;
    private Long readingsCompleted;
    private Double avgAccuracy;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String userId, String displayName, String username,
                           Long totalScore, Long readingsCompleted, Double avgAccuracy) {
        this.userId = userId;
        this.displayName = displayName;
        this.username = username;
        this.totalScore = totalScore;
        this.readingsCompleted = readingsCompleted;
        this.avgAccuracy = avgAccuracy;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getTotalScore() { return totalScore; }
    public void setTotalScore(Long totalScore) { this.totalScore = totalScore; }

    public Long getReadingsCompleted() { return readingsCompleted; }
    public void setReadingsCompleted(Long readingsCompleted) { this.readingsCompleted = readingsCompleted; }

    public Double getAvgAccuracy() { return avgAccuracy; }
    public void setAvgAccuracy(Double avgAccuracy) { this.avgAccuracy = avgAccuracy; }
}