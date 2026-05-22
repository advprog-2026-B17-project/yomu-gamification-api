package com.yomu.gamification.controller;

import com.yomu.gamification.dto.ClanBuffsResponse;
import com.yomu.gamification.service.BuffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clans")
public class BuffController {

    private final BuffService buffService;

    public BuffController(BuffService buffService) {
        this.buffService = buffService;
    }

    @GetMapping("/{clanId}/buffs")
    public ResponseEntity<ClanBuffsResponse> getClanBuffs(@PathVariable String clanId) {
        try {
            UUID clanUuid = UUID.fromString(clanId);
            ClanBuffsResponse buffs = buffService.getClanBuffs(clanUuid);
            return ResponseEntity.ok(buffs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}