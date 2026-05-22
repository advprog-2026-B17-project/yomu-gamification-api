package com.yomu.gamification.repository;

import com.yomu.gamification.entity.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, UUID> {
    Optional<UserMission> findByUserIdAndMissionIdAndDate(UUID userId, UUID missionId, LocalDate date);
    @org.springframework.data.jpa.repository.Query(value = """
        SELECT COUNT(DISTINCT um.user_id)
        FROM gamification.user_missions um
        JOIN gamification.clan_members cm ON um.user_id = cm.user_id
        WHERE cm.clan_id = :clanId AND um.date = CURRENT_DATE AND um.progress >= 1
        """, nativeQuery = true)
    int countCompletedMissionsToday(UUID clanId);
}