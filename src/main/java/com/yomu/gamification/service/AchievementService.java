package com.yomu.gamification.service;

import com.yomu.gamification.dto.AchievementDTO;
import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.dto.CreateAchievementRequest;
import com.yomu.gamification.entity.Achievement;
import com.yomu.gamification.entity.UserAchievement;
import com.yomu.gamification.exception.ConflictException;
import com.yomu.gamification.exception.ResourceNotFoundException;
import com.yomu.gamification.repository.AchievementEntityRepository;
import com.yomu.gamification.repository.AchievementRepository;
import com.yomu.gamification.repository.UserAchievementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AchievementService {

    private final AchievementEntityRepository achievementRepository;
    private final AchievementRepository achievementRowRepository;
    private final UserAchievementRepository userAchievementRepository;

    public AchievementService(AchievementEntityRepository achievementRepository,
                               AchievementRepository achievementRowRepository,
                               UserAchievementRepository userAchievementRepository) {
        this.achievementRepository = achievementRepository;
        this.achievementRowRepository = achievementRowRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    public List<AchievementRow> getUserAchievements(UUID userId) {
        return achievementRowRepository.findUnlockedAchievementsByUserId(userId);
    }

    public List<AchievementDTO> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public AchievementDTO createAchievement(CreateAchievementRequest request) {
        Achievement achievement = new Achievement();
        achievement.setName(request.getName());
        achievement.setDescription(request.getDescription());
        achievement.setMilestone(request.getMilestone());
        achievement.setAchievementType(request.getAchievementType());
        achievement.setIconUrl(request.getIconUrl());
        achievement = achievementRepository.save(achievement);
        return toDTO(achievement);
    }

    @Transactional
    public AchievementDTO updateAchievement(UUID id, CreateAchievementRequest request) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        achievement.setName(request.getName());
        achievement.setDescription(request.getDescription());
        achievement.setMilestone(request.getMilestone());
        achievement.setAchievementType(request.getAchievementType());
        achievement.setIconUrl(request.getIconUrl());
        achievement = achievementRepository.save(achievement);
        return toDTO(achievement);
    }

    @Transactional
    public void deleteAchievement(UUID id) {
        if (!achievementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Achievement not found");
        }
        achievementRepository.deleteById(id);
    }

    @Transactional
    public void updateVisibility(UUID userId, UUID achievementId, boolean visible) {
        UserAchievement userAchievement = userAchievementRepository
                .findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("User achievement not found"));
        userAchievement.setIsVisible(visible);
        userAchievementRepository.save(userAchievement);
    }

    private AchievementDTO toDTO(Achievement achievement) {
        return new AchievementDTO(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getMilestone(),
                achievement.getAchievementType(),
                achievement.getIconUrl()
        );
    }
}