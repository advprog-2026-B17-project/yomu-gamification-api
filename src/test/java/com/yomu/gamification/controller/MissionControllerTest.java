package com.yomu.gamification.controller;

import com.yomu.gamification.dto.MissionRow;
import com.yomu.gamification.service.MissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionControllerTest {

    @Mock
    private MissionService missionService;

    private MissionController controller;

    @BeforeEach
    void setUp() {
        controller = new MissionController(missionService);
    }

    @Test
    void getUserMissions_validUserId_returnsMissions() {
        UUID userId = UUID.randomUUID();
        List<MissionRow> expected = Arrays.asList(
            new MissionRow("mis1", "Read 3 articles", "Read and complete quizzes", "reading", 3, 50, 1, false, LocalDate.now()),
            new MissionRow("mis2", "Complete 2 quizzes", "Score above 80%", "quiz", 2, 100, 0, false, LocalDate.now())
        );
        when(missionService.getUserMissions(userId)).thenReturn(expected);

        ResponseEntity<List<MissionRow>> response = controller.getUserMissions(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(missionService).getUserMissions(userId);
    }

    @Test
    void getUserMissions_invalidUserId_returnsBadRequest() {
        ResponseEntity<List<MissionRow>> response = controller.getUserMissions("invalid-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(missionService);
    }
}