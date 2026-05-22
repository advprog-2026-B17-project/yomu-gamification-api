package com.yomu.gamification.service;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.dto.GamificationProfileResponse;
import com.yomu.gamification.dto.StatsDTO;
import com.yomu.gamification.repository.AchievementEntityRepository;
import com.yomu.gamification.repository.ClanRepository;
import com.yomu.gamification.repository.ClanMemberRepository;
import com.yomu.gamification.repository.UserActivityStatsRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GamificationProfileService {

    private final AchievementEntityRepository achievementRepository;
    private final ClanRepository clanRepository;
    private final ClanMemberRepository clanMemberRepository;
    private final UserActivityStatsRepository userActivityStatsRepository;

    public GamificationProfileService(AchievementEntityRepository achievementRepository,
                                      ClanRepository clanRepository,
                                      ClanMemberRepository clanMemberRepository,
                                      UserActivityStatsRepository userActivityStatsRepository) {
        this.achievementRepository = achievementRepository;
        this.clanRepository = clanRepository;
        this.clanMemberRepository = clanMemberRepository;
        this.userActivityStatsRepository = userActivityStatsRepository;
    }

    public GamificationProfileResponse getProfile(UUID userId) {
        // Get stats from activity projection
        StatsDTO stats = userActivityStatsRepository.findByUserId(userId)
                .map(stat -> new StatsDTO(
                        stat.getReadingsCompleted(),
                        stat.getQuizAttempts(),
                        stat.getAverageAccuracy()
                ))
                .orElse(new StatsDTO(0, 0, 0.0));

        // Get visible achievements
        List<Object[]> achievementResults = achievementRepository.findUnlockedAchievementsByUserId(userId);
        List<AchievementRow> visibleAchievements = achievementResults.stream()
                .map(row -> new AchievementRow(
                        row[0].toString(),
                        (String) row[1],
                        (String) row[2],
                        row[3] instanceof Number ? ((Number) row[3]).intValue() : 0,
                        (String) row[4],
                        (Boolean) row[5],
                        row[6] != null ? OffsetDateTime.parse(row[6].toString().substring(0, 10) + "T00:00:00Z") : null,
                        row[7] != null ? (Boolean) row[7] : false
                ))
                .filter(a -> Boolean.TRUE.equals(a.getVisible()))
                .toList();

        // Get clan summary
        Optional<Object[]> clanRow = clanRepository.findUserClanByUserId(userId);
        GamificationProfileResponse.ClanSummary clanSummary = clanRow.map(c -> new GamificationProfileResponse.ClanSummary(
                c[0].toString(),
                (String) c[1],
                (String) c[2],
                c[7] != null ? (String) c[7] : "member"
        )).orElse(null);

        return new GamificationProfileResponse(
                userId.toString(),
                stats,
                visibleAchievements,
                clanSummary
        );
    }
}