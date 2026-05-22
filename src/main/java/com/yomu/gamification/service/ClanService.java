package com.yomu.gamification.service;

import com.yomu.gamification.dto.ClanLeaderboardEntry;
import com.yomu.gamification.dto.ClanRow;
import com.yomu.gamification.dto.LeaderboardEntry;
import com.yomu.gamification.repository.ClanRepository;
import com.yomu.gamification.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClanService {

    private final ClanRepository clanRepository;
    private final LeaderboardRepository leaderboardRepository;

    public ClanService(ClanRepository clanRepository, LeaderboardRepository leaderboardRepository) {
        this.clanRepository = clanRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<ClanRow> getAllClans(UUID userId) {
        return clanRepository.findAllClansWithUserRole(userId);
    }

    public Optional<ClanRow> getUserClan(UUID userId) {
        return clanRepository.findUserClanByUserId(userId);
    }

    public List<ClanLeaderboardEntry> getGlobalLeaderboard() {
        return clanRepository.findGlobalClanLeaderboard();
    }

    public List<LeaderboardEntry> getClanLeaderboard(UUID clanId) {
        return leaderboardRepository.findClanLeaderboardByClanId(clanId);
    }
}