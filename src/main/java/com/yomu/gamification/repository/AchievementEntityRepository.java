package com.yomu.gamification.repository;

import com.yomu.gamification.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementEntityRepository extends JpaRepository<Achievement, UUID> {
    List<Achievement> findByMilestoneLessThanEqualAndAchievementType(int milestone, String achievementType);
    Optional<Achievement> findByAchievementType(String achievementType);
}