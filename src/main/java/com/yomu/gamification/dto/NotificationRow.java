package com.yomu.gamification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class NotificationRow {
    private String id;
    private String userId;
    private String notificationType;
    private String title;
    private String message;
    private Boolean isRead;
    private OffsetDateTime createdAt;

    public NotificationRow() {}

    public NotificationRow(String id, String userId, String notificationType, String title,
                          String message, Boolean isRead, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}