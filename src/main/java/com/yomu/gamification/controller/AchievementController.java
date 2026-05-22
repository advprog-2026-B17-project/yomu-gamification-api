package com.yomu.gamification.controller;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.service.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AchievementRow>> getUserAchievements(@PathVariable String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            List<AchievementRow> achievements = achievementService.getUserAchievements(userUuid);
            return ResponseEntity.ok(achievements);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}