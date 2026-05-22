package com.yomu.gamification.controller;

import com.yomu.gamification.dto.MissionRow;
import com.yomu.gamification.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MissionRow>> getUserMissions(@PathVariable String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            List<MissionRow> missions = missionService.getUserMissions(userUuid);
            return ResponseEntity.ok(missions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}