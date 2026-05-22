package com.yomu.gamification.controller;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.service.AchievementService;
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
class AchievementControllerTest {

    @Mock
    private AchievementService achievementService;

    private AchievementController controller;

    @BeforeEach
    void setUp() {
        controller = new AchievementController(achievementService);
    }

    @Test
    void getUserAchievements_validUserId_returnsAchievements() {
        UUID userId = UUID.randomUUID();
        List<AchievementRow> expected = Arrays.asList(
            new AchievementRow("ach1", "First Achievement", "Description 1", 10, "icon.png", true, OffsetDateTime.now(), true),
            new AchievementRow("ach2", "Second Achievement", "Description 2", 20, null, true, OffsetDateTime.now(), true)
        );
        when(achievementService.getUserAchievements(userId)).thenReturn(expected);

        ResponseEntity<List<AchievementRow>> response = controller.getUserAchievements(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(achievementService).getUserAchievements(userId);
    }

    @Test
    void getUserAchievements_invalidUserId_returnsBadRequest() {
        ResponseEntity<List<AchievementRow>> response = controller.getUserAchievements("not-a-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(achievementService);
    }

    @Test
    void getUserAchievements_userIdCaseSensitive_failure() {
        ResponseEntity<List<AchievementRow>> response = controller.getUserAchievements("550e8400-e29b-41d4-a716-446655440000");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}