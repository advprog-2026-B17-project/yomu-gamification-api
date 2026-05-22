package com.yomu.gamification.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ClanDTO {
    private UUID id;
    private String name;
    private String tier;
    private BigDecimal totalScore;
    private UUID leaderId;
    private long memberCount;
    private String myRole;

    public ClanDTO() {}

    public ClanDTO(UUID id, String name, String tier, BigDecimal totalScore,
                   UUID leaderId, long memberCount, String myRole) {
        this.id = id;
        this.name = name;
        this.tier = tier;
        this.totalScore = totalScore;
        this.leaderId = leaderId;
        this.memberCount = memberCount;
        this.myRole = myRole;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    public UUID getLeaderId() { return leaderId; }
    public void setLeaderId(UUID leaderId) { this.leaderId = leaderId; }
    public long getMemberCount() { return memberCount; }
    public void setMemberCount(long memberCount) { this.memberCount = memberCount; }
    public String getMyRole() { return myRole; }
    public void setMyRole(String myRole) { this.myRole = myRole; }
}