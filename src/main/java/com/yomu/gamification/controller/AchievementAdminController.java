package com.yomu.gamification.controller;

import com.yomu.gamification.dto.AchievementDTO;
import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.dto.CreateAchievementRequest;
import com.yomu.gamification.exception.ForbiddenException;
import com.yomu.gamification.service.AchievementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/achievements")
public class AchievementAdminController {

    private final AchievementService achievementService;

    public AchievementAdminController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        requireAdmin(userRole);
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @Valid @RequestBody CreateAchievementRequest request) {
        requireAdmin(userRole);
        return ResponseEntity.ok(achievementService.createAchievement(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id,
            @Valid @RequestBody CreateAchievementRequest request) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(achievementService.updateAchievement(uuid, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        achievementService.deleteAchievement(uuid);
        return ResponseEntity.noContent().build();
    }

    private void requireAdmin(String userRole) {
        if (!"ROLE_ADMIN".equals(userRole)) {
            throw new ForbiddenException("Admin access required");
        }
    }
}