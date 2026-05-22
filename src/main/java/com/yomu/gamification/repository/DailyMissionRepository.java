package com.yomu.gamification.repository;

import com.yomu.gamification.entity.DailyMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DailyMissionRepository extends JpaRepository<DailyMission, UUID> {
    List<DailyMission> findByIsActiveTrue();
}