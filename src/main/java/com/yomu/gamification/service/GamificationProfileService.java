package com.yomu.gamification.service;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.dto.ClanRow;
import com.yomu.gamification.dto.GamificationProfileResponse;
import com.yomu.gamification.repository.AchievementEntityRepository;
import com.yomu.gamification.repository.ClanRepository;
import com.yomu.gamification.repository.ClanMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GamificationProfileService {

    private final AchievementEntityRepository achievementRepository;
    private final ClanRepository clanRepository;
    private final ClanMemberRepository clanMemberRepository;

    public GamificationProfileService(AchievementEntityRepository achievementRepository,
                                      ClanRepository clanRepository,
                                      ClanMemberRepository clanMemberRepository) {
        this.achievementRepository = achievementRepository;
        this.clanRepository = clanRepository;
        this.clanMemberRepository = clanMemberRepository;
    }

    public GamificationProfileResponse getProfile(UUID userId) {
        List<AchievementRow> unlockedAchievements = achievementRepository.findUnlockedAchievementsByUserId(userId);
        List<AchievementRow> visibleAchievements = unlockedAchievements.stream()
                .filter(a -> Boolean.TRUE.equals(a.getVisible()))
                .toList();

        Optional<ClanRow> clanRow = clanRepository.findUserClanByUserId(userId);
        GamificationProfileResponse.ClanSummary clanSummary = clanRow.map(c -> new GamificationProfileResponse.ClanSummary(
                c.getId(),
                c.getName(),
                c.getTier(),
                c.getMyRole() != null ? c.getMyRole() : "member"
        )).orElse(null);

        return new GamificationProfileResponse(
                userId.toString(),
                visibleAchievements,
                clanSummary
        );
    }
}