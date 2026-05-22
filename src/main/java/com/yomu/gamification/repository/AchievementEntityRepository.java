package com.yomu.gamification.repository;

import com.yomu.gamification.dto.AchievementRow;
import com.yomu.gamification.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AchievementEntityRepository extends JpaRepository<Achievement, UUID> {
    List<Achievement> findByMilestoneLessThanEqualAndAchievementType(int milestone, String achievementType);
    Optional<Achievement> findByAchievementType(String achievementType);

    @Query(value = """
        SELECT a.id::text as id, a.name, a.description, a.milestone, a.icon_url,
               true as unlocked, ua.unlocked_at, ua.is_visible as visible
        FROM gamification.achievements a
        JOIN gamification.user_achievements ua ON a.id = ua.achievement_id
        WHERE ua.user_id = :userId
        ORDER BY ua.unlocked_at DESC
        """, nativeQuery = true)
    List<AchievementRow> findUnlockedAchievementsByUserId(@Param("userId") UUID userId);
}