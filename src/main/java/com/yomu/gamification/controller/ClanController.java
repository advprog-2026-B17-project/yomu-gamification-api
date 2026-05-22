package com.yomu.gamification.controller;

import com.yomu.gamification.dto.*;
import com.yomu.gamification.service.ClanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clans")
public class ClanController {

    private final ClanService clanService;

    public ClanController(ClanService clanService) {
        this.clanService = clanService;
    }

    @GetMapping
    public ResponseEntity<List<ClanRow>> getAllClans(@RequestAttribute("userId") String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            List<ClanRow> clans = clanService.getAllClans(userUuid);
            return ResponseEntity.ok(clans);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ClanRow> getMyClan(@RequestAttribute("userId") String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            return clanService.getUserClan(userUuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<ClanLeaderboardEntry>> getGlobalLeaderboard() {
        List<ClanLeaderboardEntry> leaderboard = clanService.getGlobalLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/{clanId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getClanLeaderboard(@PathVariable String clanId) {
        try {
            UUID clanUuid = UUID.fromString(clanId);
            List<LeaderboardEntry> leaderboard = clanService.getClanLeaderboard(clanUuid);
            return ResponseEntity.ok(leaderboard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ClanDTO> createClan(
            @RequestAttribute("userId") String userId,
            @Valid @RequestBody CreateClanRequest request) {
        try {
            UUID userUuid = UUID.fromString(userId);
            ClanDTO clan = clanService.createClan(userUuid, request);
            return ResponseEntity.ok(clan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinClan(
            @PathVariable String id,
            @RequestAttribute("userId") String userId) {
        try {
            UUID clanUuid = UUID.fromString(id);
            UUID userUuid = UUID.fromString(userId);
            clanService.joinClan(userUuid, clanUuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leaveClan(
            @PathVariable String id,
            @RequestAttribute("userId") String userId) {
        try {
            UUID clanUuid = UUID.fromString(id);
            UUID userUuid = UUID.fromString(userId);
            clanService.leaveClan(userUuid, clanUuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClan(
            @PathVariable String id,
            @RequestAttribute("userId") String userId,
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        try {
            UUID clanUuid = UUID.fromString(id);
            UUID userUuid = UUID.fromString(userId);
            boolean isAdmin = "ROLE_ADMIN".equals(userRole);
            clanService.deleteClan(userUuid, clanUuid, isAdmin);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}