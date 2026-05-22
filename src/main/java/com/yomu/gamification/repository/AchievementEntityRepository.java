package com.yomu.gamification.repository;

import com.yomu.gamification.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AchievementEntityRepository extends JpaRepository<Achievement, UUID> {
}