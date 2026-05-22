package com.yomu.gamification.service;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.repository.AchievementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementRepository achievementRepository;

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    public List<AchievementRow> getUserAchievements(UUID userId) {
        return achievementRepository.findUnlockedAchievementsByUserId(userId);
    }
}