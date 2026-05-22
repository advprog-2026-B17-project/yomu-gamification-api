package com.yomu.gamification.dto;

import java.util.List;

public class GamificationProfileResponse {
    private String userId;
    private List<AchievementRow> visibleAchievements;
    private ClanSummary clanSummary;

    public GamificationProfileResponse() {}

    public GamificationProfileResponse(String userId, List<AchievementRow> visibleAchievements, ClanSummary clanSummary) {
        this.userId = userId;
        this.visibleAchievements = visibleAchievements;
        this.clanSummary = clanSummary;
    }

    public static class ClanSummary {
        private String id;
        private String name;
        private String tier;
        private String role;

        public ClanSummary() {}

        public ClanSummary(String id, String name, String tier, String role) {
            this.id = id;
            this.name = name;
            this.tier = tier;
            this.role = role;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getTier() { return tier; }
        public void setTier(String tier) { this.tier = tier; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<AchievementRow> getVisibleAchievements() { return visibleAchievements; }
    public void setVisibleAchievements(List<AchievementRow> visibleAchievements) { this.visibleAchievements = visibleAchievements; }
    public ClanSummary getClanSummary() { return clanSummary; }
    public void setClanSummary(ClanSummary clanSummary) { this.clanSummary = clanSummary; }
}