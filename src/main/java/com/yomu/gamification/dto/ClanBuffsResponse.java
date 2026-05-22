package com.yomu.gamification.dto;

import java.util.List;

public class ClanBuffsResponse {
    private String clanId;
    private List<BuffDTO> buffs;

    public ClanBuffsResponse() {}

    public ClanBuffsResponse(String clanId, List<BuffDTO> buffs) {
        this.clanId = clanId;
        this.buffs = buffs;
    }

    public String getClanId() { return clanId; }
    public void setClanId(String clanId) { this.clanId = clanId; }
    public List<BuffDTO> getBuffs() { return buffs; }
    public void setBuffs(List<BuffDTO> buffs) { this.buffs = buffs; }
}