package com.yomu.gamification.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_activity_stats", schema = "gamification")
public class UserActivityStats {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "readings_completed", nullable = false)
    private Integer readingsCompleted = 0;

    @Column(name = "quiz_attempts", nullable = false)
    private Integer quizAttempts = 0;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore = 0;

    @Column(name = "average_accuracy", nullable = false)
    private Double averageAccuracy = 0.0;

    @Column(name = "last_activity_at")
    private OffsetDateTime lastActivityAt;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public Integer getReadingsCompleted() { return readingsCompleted; }
    public void setReadingsCompleted(Integer readingsCompleted) { this.readingsCompleted = readingsCompleted; }
    public Integer getQuizAttempts() { return quizAttempts; }
    public void setQuizAttempts(Integer quizAttempts) { this.quizAttempts = quizAttempts; }
    public Integer getTotalScore() { return totalScore; }
    public void setTotalScore(Integer totalScore) { this.totalScore = totalScore; }
    public Double getAverageAccuracy() { return averageAccuracy; }
    public void setAverageAccuracy(Double averageAccuracy) { this.averageAccuracy = averageAccuracy; }
    public OffsetDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(OffsetDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
}