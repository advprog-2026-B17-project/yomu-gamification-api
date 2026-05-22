package com.yomu.gamification.service;

import com.yomu.gamification.entity.*;
import com.yomu.gamification.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EventConsumerService {

    private static final Logger log = LoggerFactory.getLogger(EventConsumerService.class);

    private final ProcessedEventRepository processedEventRepository;
    private final UserProfileRepository userProfileRepository;
    private final ReadingCatalogProjectionRepository readingCatalogRepository;
    private final UserActivityStatsRepository userActivityStatsRepository;
    private final DailyMissionRepository dailyMissionRepository;
    private final UserMissionRepository userMissionRepository;
    private final AchievementEntityRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final NotificationEntityRepository notificationRepository;
    private final ClanMemberRepository clanMemberRepository;
    private final BuffRepository buffRepository;

    public EventConsumerService(
            ProcessedEventRepository processedEventRepository,
            UserProfileRepository userProfileRepository,
            ReadingCatalogProjectionRepository readingCatalogRepository,
            UserActivityStatsRepository userActivityStatsRepository,
            DailyMissionRepository dailyMissionRepository,
            UserMissionRepository userMissionRepository,
            AchievementEntityRepository achievementRepository,
            UserAchievementRepository userAchievementRepository,
            NotificationEntityRepository notificationRepository,
            ClanMemberRepository clanMemberRepository,
            BuffRepository buffRepository) {
        this.processedEventRepository = processedEventRepository;
        this.userProfileRepository = userProfileRepository;
        this.readingCatalogRepository = readingCatalogRepository;
        this.userActivityStatsRepository = userActivityStatsRepository;
        this.dailyMissionRepository = dailyMissionRepository;
        this.userMissionRepository = userMissionRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.notificationRepository = notificationRepository;
        this.clanMemberRepository = clanMemberRepository;
        this.buffRepository = buffRepository;
    }

    public boolean isEventProcessed(String eventId) {
        return processedEventRepository.existsByEventId(eventId);
    }

    @Transactional
    public void markEventProcessed(String eventId, String eventType) {
        if (!processedEventRepository.existsByEventId(eventId)) {
            ProcessedEvent event = new ProcessedEvent(eventId, eventType);
            processedEventRepository.save(event);
        }
    }

    // === User Events ===

    @SuppressWarnings("unchecked")
    public void handleUserCreated(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String userId = (String) payload.get("userId");
        String username = (String) payload.get("username");
        String displayName = (String) payload.get("displayName");
        String role = (String) payload.getOrDefault("role", "student");

        log.info("Creating user profile for userId: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(UUID.fromString(userId))
                .orElse(new UserProfile());
        profile.setUserId(UUID.fromString(userId));
        profile.setUsername(username);
        profile.setDisplayName(displayName);
        profile.setRole(role);
        profile.setDeleted(false);
        profile.setUpdatedAt(OffsetDateTime.now());
        userProfileRepository.save(profile);
    }

    @SuppressWarnings("unchecked")
    public void handleUserUpdated(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String userId = (String) payload.get("userId");
        String username = (String) payload.get("username");
        String displayName = (String) payload.get("displayName");
        String role = (String) payload.getOrDefault("role", "student");

        log.info("Updating user profile for userId: {}", userId);

        userProfileRepository.findByUserId(UUID.fromString(userId)).ifPresent(profile -> {
            profile.setUsername(username);
            profile.setDisplayName(displayName);
            profile.setRole(role);
            profile.setUpdatedAt(OffsetDateTime.now());
            userProfileRepository.save(profile);
        });
    }

    @SuppressWarnings("unchecked")
    public void handleUserDeleted(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String userId = (String) payload.get("userId");

        log.info("Marking user profile as deleted for userId: {}", userId);

        userProfileRepository.findByUserId(UUID.fromString(userId)).ifPresent(profile -> {
            profile.setDeleted(true);
            profile.setUpdatedAt(OffsetDateTime.now());
            userProfileRepository.save(profile);
        });
    }

    // === Reading Events ===

    @SuppressWarnings("unchecked")
    public void handleReadingCreated(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String readingId = (String) payload.get("readingId");
        String title = (String) payload.get("title");
        String categoryName = (String) payload.getOrDefault("categoryName", "");

        log.info("Creating reading projection for readingId: {}", readingId);

        ReadingCatalogProjection projection = readingCatalogRepository.findByReadingId(UUID.fromString(readingId))
                .orElse(new ReadingCatalogProjection());
        projection.setReadingId(UUID.fromString(readingId));
        projection.setTitle(title);
        projection.setCategoryName(categoryName);
        projection.setDeleted(false);
        projection.setUpdatedAt(OffsetDateTime.now());
        readingCatalogRepository.save(projection);
    }

    @SuppressWarnings("unchecked")
    public void handleReadingUpdated(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String readingId = (String) payload.get("readingId");
        String title = (String) payload.get("title");
        String categoryName = (String) payload.getOrDefault("categoryName", "");

        log.info("Updating reading projection for readingId: {}", readingId);

        readingCatalogRepository.findByReadingId(UUID.fromString(readingId)).ifPresent(projection -> {
            projection.setTitle(title);
            projection.setCategoryName(categoryName);
            projection.setUpdatedAt(OffsetDateTime.now());
            readingCatalogRepository.save(projection);
        });
    }

    @SuppressWarnings("unchecked")
    public void handleReadingDeleted(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String readingId = (String) payload.get("readingId");

        log.info("Marking reading projection as deleted for readingId: {}", readingId);

        readingCatalogRepository.findByReadingId(UUID.fromString(readingId)).ifPresent(projection -> {
            projection.setDeleted(true);
            projection.setUpdatedAt(OffsetDateTime.now());
            readingCatalogRepository.save(projection);
        });
    }

    // === Quiz/Reading Completion Events ===

    @SuppressWarnings("unchecked")
    public void handleQuizCompleted(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String userId = (String) payload.get("userId");
        String readingId = (String) payload.get("readingId");
        int score = ((Number) payload.get("score")).intValue();
        double accuracy = ((Number) payload.get("accuracy")).doubleValue();

        log.info("Processing quiz completed for userId: {}, readingId: {}, score: {}", userId, readingId, score);

        UUID userUuid = UUID.fromString(userId);

        // Update user activity stats
        updateUserActivityStats(userUuid, score, accuracy);

        // Update mission progress
        updateMissionProgress(userUuid, "quiz");

        // Check reading count achievements
        checkReadingCountAchievements(userUuid);

        // Check perfect score achievement
        if (score == 100) {
            checkAndUnlockPerfectScoreAchievement(userUuid);
        }

        // Recalculate clan buffs if user is in a clan
        recalculateClanBuffs(userUuid);
    }

    @SuppressWarnings("unchecked")
    public void handleReadingCompleted(Map<String, Object> message) {
        Map<String, Object> payload = (Map<String, Object>) message.get("payload");
        String userId = (String) payload.get("userId");

        log.info("Processing reading completed for userId: {}", userId);

        UUID userUuid = UUID.fromString(userId);

        // Update mission progress
        updateMissionProgress(userUuid, "reading");

        // Recalculate clan buffs
        recalculateClanBuffs(userUuid);
    }

    // === Helper Methods ===

    private void updateUserActivityStats(UUID userId, int score, double accuracy) {
        UserActivityStats stats = userActivityStatsRepository.findByUserId(userId)
                .orElse(new UserActivityStats());

        stats.setUserId(userId);
        stats.setReadingsCompleted(stats.getReadingsCompleted() + 1);
        stats.setQuizAttempts(stats.getQuizAttempts() + 1);
        stats.setTotalScore(stats.getTotalScore() + score);
        stats.setAverageAccuracy(calculateNewAverage(stats, accuracy));
        stats.setLastActivityAt(OffsetDateTime.now());

        userActivityStatsRepository.save(stats);
    }

    private double calculateNewAverage(UserActivityStats stats, double newAccuracy) {
        int totalAttempts = stats.getQuizAttempts();
        if (totalAttempts == 0) return newAccuracy;

        double currentTotalAccuracy = stats.getAverageAccuracy() * (totalAttempts - 1);
        return (currentTotalAccuracy + newAccuracy) / totalAttempts;
    }

    private void updateMissionProgress(UUID userId, String targetType) {
        List<DailyMission> activeMissions = dailyMissionRepository.findByIsActiveTrue();

        for (DailyMission mission : activeMissions) {
            if (mission.getTargetType().equals(targetType) ||
                mission.getTargetType().equals("quiz") && targetType.equals("quiz") ||
                mission.getTargetType().equals("reading") && targetType.equals("reading")) {

                userMissionRepository.findByUserIdAndMissionIdAndDate(userId, mission.getId(), java.time.LocalDate.now())
                        .ifPresentOrElse(
                            um -> {
                                if (!um.getClaimed() && um.getProgress() < mission.getTargetCount()) {
                                    um.setProgress(um.getProgress() + 1);
                                    userMissionRepository.save(um);
                                }
                            },
                            () -> {
                                UserMission newUm = new UserMission();
                                newUm.setUserId(userId);
                                newUm.setMissionId(mission.getId());
                                newUm.setProgress(1);
                                newUm.setClaimed(false);
                                newUm.setDate(java.time.LocalDate.now());
                                userMissionRepository.save(newUm);
                            }
                        );
            }
        }
    }

    private void checkReadingCountAchievements(UUID userId) {
        int completedReadings = userActivityStatsRepository.findByUserId(userId)
                .map(UserActivityStats::getReadingsCompleted).orElse(0);

        List<Achievement> achievements = achievementRepository.findByMilestoneLessThanEqualAndAchievementType(
                completedReadings, "reading_count");

        for (Achievement achievement : achievements) {
            if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
                unlockAchievement(userId, achievement);
            }
        }
    }

    private void checkAndUnlockPerfectScoreAchievement(UUID userId) {
        achievementRepository.findByAchievementType("quiz_perfect").ifPresent(achievement -> {
            if (!userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId())) {
                unlockAchievement(userId, achievement);
            }
        });
    }

    private void unlockAchievement(UUID userId, Achievement achievement) {
        log.info("Unlocking achievement {} for user {}", achievement.getName(), userId);

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(userId);
        userAchievement.setAchievementId(achievement.getId());
        userAchievement.setUnlockedAt(OffsetDateTime.now());
        userAchievement.setIsVisible(true);
        userAchievementRepository.save(userAchievement);

        // Create notification
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType("achievement_unlocked");
        notification.setTitle("Achievement Unlocked: " + achievement.getName());
        notification.setMessage(achievement.getDescription() != null ? achievement.getDescription() : "You unlocked a new achievement!");
        notification.setIsRead(false);
        notification.setCreatedAt(OffsetDateTime.now());
        notificationRepository.save(notification);
    }

    private void recalculateClanBuffs(UUID userId) {
        clanMemberRepository.findByUserId(userId).ifPresent(member -> {
            recalculateClanBuffsForClan(member.getClanId());
        });
    }

    private void recalculateClanBuffsForClan(UUID clanId) {
        long memberCount = clanMemberRepository.countByClanId(clanId);
        if (memberCount == 0) return;

        // Calculate Productivity Buff
        long completedToday = userMissionRepository.countCompletedMissionsToday(clanId);
        double productivityRatio = (double) completedToday / memberCount;

        updateBuff(clanId, "productivity_buff", productivityRatio >= 0.5 ? 1.2 : 1.0);

        // Additional buff calculations could go here based on accuracy, activity, etc.
    }

    private void updateBuff(UUID clanId, String buffType, double multiplier) {
        // Deactivate existing buff of same type
        buffRepository.findByClanIdAndBuffTypeAndExpiresAtIsNull(clanId, buffType)
                .ifPresent(buff -> {
                    buff.setExpiresAt(OffsetDateTime.now());
                    buffRepository.save(buff);
                });

        // Only add new buff if multiplier != 1.0
        if (Math.abs(multiplier - 1.0) > 0.001) {
            Buff newBuff = new Buff();
            newBuff.setClanId(clanId);
            newBuff.setBuffType(buffType);
            newBuff.setMultiplier(multiplier);
            newBuff.setActivatedAt(OffsetDateTime.now());
            buffRepository.save(newBuff);
        }
    }
}