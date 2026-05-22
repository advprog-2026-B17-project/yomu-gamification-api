package com.yomu.gamification.controller;

import com.yomu.gamification.dto.GamificationProfileResponse;
import com.yomu.gamification.service.GamificationProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/gamification/profiles")
public class GamificationProfileController {

    private final GamificationProfileService profileService;

    public GamificationProfileController(GamificationProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GamificationProfileResponse> getProfile(@PathVariable String userId) {
        UUID userUuid = UUID.fromString(userId);
        return ResponseEntity.ok(profileService.getProfile(userUuid));
    }
}