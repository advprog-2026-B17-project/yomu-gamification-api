package com.yomu.gamification.repository;

import com.yomu.gamification.entity.UserActivityStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserActivityStatsRepository extends JpaRepository<UserActivityStats, UUID> {
    Optional<UserActivityStats> findByUserId(UUID userId);
}