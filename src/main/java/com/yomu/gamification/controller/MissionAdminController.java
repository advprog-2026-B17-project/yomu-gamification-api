package com.yomu.gamification.controller;

import com.yomu.gamification.dto.CreateMissionRequest;
import com.yomu.gamification.dto.DailyMissionDTO;
import com.yomu.gamification.dto.UserMissionDTO;
import com.yomu.gamification.exception.ForbiddenException;
import com.yomu.gamification.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/missions")
public class MissionAdminController {

    private final MissionService missionService;

    public MissionAdminController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public ResponseEntity<List<DailyMissionDTO>> getAllMissions(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        requireAdmin(userRole);
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DailyMissionDTO>> getActiveMissions(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        requireAdmin(userRole);
        return ResponseEntity.ok(missionService.getActiveMissions());
    }

    @PostMapping
    public ResponseEntity<DailyMissionDTO> createMission(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @Valid @RequestBody CreateMissionRequest request) {
        requireAdmin(userRole);
        return ResponseEntity.ok(missionService.createMission(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyMissionDTO> updateMission(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id,
            @Valid @RequestBody CreateMissionRequest request) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(missionService.updateMission(uuid, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        missionService.deleteMission(uuid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<DailyMissionDTO> toggleMission(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        return ResponseEntity.ok(missionService.toggleMissionActive(uuid));
    }

    private void requireAdmin(String userRole) {
        if (!"ROLE_ADMIN".equals(userRole)) {
            throw new ForbiddenException("Admin access required");
        }
    }
}