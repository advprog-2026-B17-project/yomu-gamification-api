package com.yomu.gamification.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "buffs", schema = "gamification")
public class Buff {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "clan_id", nullable = false)
    private UUID clanId;

    @Column(name = "buff_type", nullable = false, length = 50)
    private String buffType;

    @Column(nullable = false)
    private Double multiplier;

    @Column(name = "activated_at")
    private OffsetDateTime activatedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getClanId() { return clanId; }
    public void setClanId(UUID clanId) { this.clanId = clanId; }
    public String getBuffType() { return buffType; }
    public void setBuffType(String buffType) { this.buffType = buffType; }
    public Double getMultiplier() { return multiplier; }
    public void setMultiplier(Double multiplier) { this.multiplier = multiplier; }
    public OffsetDateTime getActivatedAt() { return activatedAt; }
    public void setActivatedAt(OffsetDateTime activatedAt) { this.activatedAt = activatedAt; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }
}