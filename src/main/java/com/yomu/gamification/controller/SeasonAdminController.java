package com.yomu.gamification.controller;

import com.yomu.gamification.dto.CreateSeasonRequest;
import com.yomu.gamification.dto.SeasonDTO;
import com.yomu.gamification.exception.ForbiddenException;
import com.yomu.gamification.service.SeasonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/seasons")
public class SeasonAdminController {

    private final SeasonService seasonService;

    public SeasonAdminController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping
    public ResponseEntity<List<SeasonDTO>> getAllSeasons(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        requireAdmin(userRole);
        return ResponseEntity.ok(seasonService.getAllSeasons());
    }

    @GetMapping("/active")
    public ResponseEntity<SeasonDTO> getActiveSeason(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole) {
        requireAdmin(userRole);
        return seasonService.getActiveSeason()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SeasonDTO> createSeason(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @Valid @RequestBody CreateSeasonRequest request) {
        requireAdmin(userRole);
        return ResponseEntity.ok(seasonService.createSeason(request));
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<SeasonDTO> endSeason(
            @RequestAttribute(value = "authenticated.userRole", required = false) String userRole,
            @PathVariable String id) {
        requireAdmin(userRole);
        UUID uuid = UUID.fromString(id);
        return seasonService.endSeason(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private void requireAdmin(String userRole) {
        if (!"ROLE_ADMIN".equals(userRole)) {
            throw new ForbiddenException("Admin access required");
        }
    }
}