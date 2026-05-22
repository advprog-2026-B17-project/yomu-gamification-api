package com.yomu.gamification.controller;

import com.yomu.gamification.dto.NotificationRow;
import com.yomu.gamification.dto.StatusResponse;
import com.yomu.gamification.dto.UnreadCountResponse;
import com.yomu.gamification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private NotificationController controller;

    @BeforeEach
    void setUp() {
        controller = new NotificationController(notificationService);
    }

    @Test
    void getUserNotifications_validUserId_returnsNotifications() {
        UUID userId = UUID.randomUUID();
        List<NotificationRow> expected = Arrays.asList(
            new NotificationRow("notif1", userId.toString(), "achievement", "Achievement Unlocked", "You unlocked First Achievement!", false, OffsetDateTime.now()),
            new NotificationRow("notif2", userId.toString(), "mission", "Daily Mission Complete", "You completed your daily mission!", true, OffsetDateTime.now())
        );
        when(notificationService.getUserNotifications(userId)).thenReturn(expected);

        ResponseEntity<List<NotificationRow>> response = controller.getUserNotifications(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getUserNotifications_invalidUserId_returnsBadRequest() {
        ResponseEntity<List<NotificationRow>> response = controller.getUserNotifications("invalid-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(notificationService);
    }

    @Test
    void getUnreadCount_validUserId_returnsCount() {
        UUID userId = UUID.randomUUID();
        when(notificationService.getUnreadCount(userId)).thenReturn(5L);

        ResponseEntity<UnreadCountResponse> response = controller.getUnreadCount(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5L, response.getBody().getCount());
    }

    @Test
    void markAsRead_validIds_returnsOk() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(notificationService.markNotificationAsRead(notificationId, userId)).thenReturn(true);

        ResponseEntity<StatusResponse> response = controller.markAsRead(notificationId.toString(), userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ok", response.getBody().getStatus());
    }

    @Test
    void markAsRead_notificationNotFound_returnsNotFound() {
        UUID notificationId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(notificationService.markNotificationAsRead(notificationId, userId)).thenReturn(false);

        ResponseEntity<StatusResponse> response = controller.markAsRead(notificationId.toString(), userId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void markAsRead_invalidNotificationId_returnsBadRequest() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<StatusResponse> response = controller.markAsRead("not-a-uuid", userId.toString());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(notificationService);
    }
}