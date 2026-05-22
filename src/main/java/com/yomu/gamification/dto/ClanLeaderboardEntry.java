package com.yomu.gamification.dto;

public class ClanLeaderboardEntry {
    private String clanId;
    private String clanName;
    private String tier;
    private Double totalScore;
    private Long memberCount;
    private Double multiplier;
    private Double effectiveScore;

    public ClanLeaderboardEntry() {}

    public ClanLeaderboardEntry(String clanId, String clanName, String tier, Double totalScore,
                               Long memberCount, Double multiplier, Double effectiveScore) {
        this.clanId = clanId;
        this.clanName = clanName;
        this.tier = tier;
        this.totalScore = totalScore;
        this.memberCount = memberCount;
        this.multiplier = multiplier;
        this.effectiveScore = effectiveScore;
    }

    public String getClanId() { return clanId; }
    public void setClanId(String clanId) { this.clanId = clanId; }

    public String getClanName() { return clanName; }
    public void setClanName(String clanName) { this.clanName = clanName; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }

    public Long getMemberCount() { return memberCount; }
    public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }

    public Double getMultiplier() { return multiplier; }
    public void setMultiplier(Double multiplier) { this.multiplier = multiplier; }

    public Double getEffectiveScore() { return effectiveScore; }
    public void setEffectiveScore(Double effectiveScore) { this.effectiveScore = effectiveScore; }
}