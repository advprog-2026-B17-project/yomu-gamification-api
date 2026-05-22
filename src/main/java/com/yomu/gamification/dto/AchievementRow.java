package com.yomu.gamification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class AchievementRow {
    private String id;
    private String name;
    private String description;
    private Integer milestone;
    private String iconUrl;
    private Boolean unlocked;
    private OffsetDateTime unlockedAt;
    private Boolean visible;

    public AchievementRow() {}

    public AchievementRow(String id, String name, String description, Integer milestone,
                         String iconUrl, Boolean unlocked, OffsetDateTime unlockedAt, Boolean visible) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.milestone = milestone;
        this.iconUrl = iconUrl;
        this.unlocked = unlocked;
        this.unlockedAt = unlockedAt;
        this.visible = visible;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getMilestone() { return milestone; }
    public void setMilestone(Integer milestone) { this.milestone = milestone; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Boolean getUnlocked() { return unlocked; }
    public void setUnlocked(Boolean unlocked) { this.unlocked = unlocked; }

    public OffsetDateTime getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(OffsetDateTime unlockedAt) { this.unlockedAt = unlockedAt; }

    public Boolean getVisible() { return visible; }
    public void setVisible(Boolean visible) { this.visible = visible; }
}