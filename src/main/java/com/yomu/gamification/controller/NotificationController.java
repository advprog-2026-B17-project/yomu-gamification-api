package com.yomu.gamification.controller;

import com.yomu.gamification.dto.NotificationRow;
import com.yomu.gamification.dto.StatusResponse;
import com.yomu.gamification.dto.UnreadCountResponse;
import com.yomu.gamification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationRow>> getUserNotifications(@PathVariable String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            List<NotificationRow> notifications = notificationService.getUserNotifications(userUuid);
            return ResponseEntity.ok(notifications);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<UnreadCountResponse> getUnreadCount(@PathVariable String userId) {
        try {
            UUID userUuid = UUID.fromString(userId);
            Long count = notificationService.getUnreadCount(userUuid);
            return ResponseEntity.ok(new UnreadCountResponse(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<StatusResponse> markAsRead(
            @PathVariable String notificationId,
            @RequestAttribute("userId") String userId) {
        try {
            UUID notificationUuid = UUID.fromString(notificationId);
            UUID userUuid = UUID.fromString(userId);
            boolean updated = notificationService.markNotificationAsRead(notificationUuid, userUuid);
            if (updated) {
                return ResponseEntity.ok(new StatusResponse("ok"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}