package com.yomu.gamification.dto;

public class ClanRow {
    private String id;
    private String name;
    private String tier;
    private Double totalScore;
    private String leaderId;
    private String leaderName;
    private Long memberCount;
    private String myRole;

    public ClanRow() {}

    public ClanRow(String id, String name, String tier, Double totalScore, String leaderId,
                  String leaderName, Long memberCount, String myRole) {
        this.id = id;
        this.name = name;
        this.tier = tier;
        this.totalScore = totalScore;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.memberCount = memberCount;
        this.myRole = myRole;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }

    public String getLeaderId() { return leaderId; }
    public void setLeaderId(String leaderId) { this.leaderId = leaderId; }

    public String getLeaderName() { return leaderName; }
    public void setLeaderName(String leaderName) { this.leaderName = leaderName; }

    public Long getMemberCount() { return memberCount; }
    public void setMemberCount(Long memberCount) { this.memberCount = memberCount; }

    public String getMyRole() { return myRole; }
    public void setMyRole(String myRole) { this.myRole = myRole; }
}