package com.yomu.gamification.controller;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.exception.ForbiddenException;
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

    @PatchMapping("/{achievementId}/visibility")
    public ResponseEntity<Void> updateVisibility(
            @PathVariable String achievementId,
            @RequestAttribute("userId") String userId,
            @RequestBody VisibilityRequest request) {
        try {
            UUID achievementUuid = UUID.fromString(achievementId);
            UUID userUuid = UUID.fromString(userId);
            achievementService.updateVisibility(userUuid, achievementUuid, request.isVisible());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class VisibilityRequest {
        private boolean visible;

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }
    }
}