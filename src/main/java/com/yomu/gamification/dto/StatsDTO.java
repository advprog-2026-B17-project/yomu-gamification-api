package com.yomu.gamification.dto;

public class StatsDTO {
    private long readingsCompleted;
    private long quizzesTaken;
    private double averageAccuracy;

    public StatsDTO() {}

    public StatsDTO(long readingsCompleted, long quizzesTaken, double averageAccuracy) {
        this.readingsCompleted = readingsCompleted;
        this.quizzesTaken = quizzesTaken;
        this.averageAccuracy = averageAccuracy;
    }

    public long getReadingsCompleted() { return readingsCompleted; }
    public void setReadingsCompleted(long readingsCompleted) { this.readingsCompleted = readingsCompleted; }
    public long getQuizzesTaken() { return quizzesTaken; }
    public void setQuizzesTaken(long quizzesTaken) { this.quizzesTaken = quizzesTaken; }
    public double getAverageAccuracy() { return averageAccuracy; }
    public void setAverageAccuracy(double averageAccuracy) { this.averageAccuracy = averageAccuracy; }
}