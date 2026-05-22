package com.yomu.gamification.controller;

import com.yomu.gamification.dto.ClanLeaderboardEntry;
import com.yomu.gamification.dto.ClanRow;
import com.yomu.gamification.dto.LeaderboardEntry;
import com.yomu.gamification.service.ClanService;
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
}