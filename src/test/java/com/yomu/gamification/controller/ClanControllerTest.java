package com.yomu.gamification.controller;

import com.yomu.gamification.dto.ClanLeaderboardEntry;
import com.yomu.gamification.dto.ClanRow;
import com.yomu.gamification.dto.LeaderboardEntry;
import com.yomu.gamification.service.ClanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClanControllerTest {

    @Mock
    private ClanService clanService;

    private ClanController controller;

    @BeforeEach
    void setUp() {
        controller = new ClanController(clanService);
    }

    @Test
    void getAllClans_validUserId_returnsClans() {
        UUID userId = UUID.randomUUID();
        List<ClanRow> expected = Arrays.asList(
            new ClanRow("clan1", "Alpha Clan", "gold", 1000.0, "leader1", "Leader One", 10L, "member"),
            new ClanRow("clan2", "Beta Clan", "silver", 500.0, "leader2", "Leader Two", 5L, null)
        );
        when(clanService.getAllClans(userId)).thenReturn(expected);

        ResponseEntity<List<ClanRow>> response = controller.getAllClans(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(clanService).getAllClans(userId);
    }

    @Test
    void getMyClan_userHasClan_returnsClan() {
        UUID userId = UUID.randomUUID();
        ClanRow clan = new ClanRow("clan1", "Alpha Clan", "gold", 1000.0, "leader1", "Leader One", 10L, "admin");
        when(clanService.getUserClan(userId)).thenReturn(Optional.of(clan));

        ResponseEntity<ClanRow> response = controller.getMyClan(userId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alpha Clan", response.getBody().getName());
    }

    @Test
    void getMyClan_userHasNoClan_returnsNotFound() {
        UUID userId = UUID.randomUUID();
        when(clanService.getUserClan(userId)).thenReturn(Optional.empty());

        ResponseEntity<ClanRow> response = controller.getMyClan(userId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getGlobalLeaderboard_returnsLeaderboard() {
        List<ClanLeaderboardEntry> expected = Arrays.asList(
            new ClanLeaderboardEntry("clan1", "Alpha Clan", "gold", 1000.0, 10L, 1.5, 1500.0),
            new ClanLeaderboardEntry("clan2", "Beta Clan", "silver", 500.0, 5L, 1.0, 500.0)
        );
        when(clanService.getGlobalLeaderboard()).thenReturn(expected);

        ResponseEntity<List<ClanLeaderboardEntry>> response = controller.getGlobalLeaderboard();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getClanLeaderboard_validClanId_returnsLeaderboard() {
        UUID clanId = UUID.randomUUID();
        List<LeaderboardEntry> expected = Arrays.asList(
            new LeaderboardEntry("user1", "User One", "userone", 100L, 10L, 85.5),
            new LeaderboardEntry("user2", "User Two", "usertwo", 80L, 8L, 80.0)
        );
        when(clanService.getClanLeaderboard(clanId)).thenReturn(expected);

        ResponseEntity<List<LeaderboardEntry>> response = controller.getClanLeaderboard(clanId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getClanLeaderboard_invalidClanId_returnsBadRequest() {
        ResponseEntity<List<LeaderboardEntry>> response = controller.getClanLeaderboard("not-a-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(clanService);
    }
}