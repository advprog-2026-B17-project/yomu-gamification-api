package com.yomu.gamification.repository;

import com.yomu.gamification.dto.MissionRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MissionRepository extends JpaRepository<MissionRow, UUID> {

    @Query(value = """
        SELECT dm.id::text as id, dm.title, dm.description, dm.target_type, dm.target_count, dm.xp_reward,
               COALESCE(um.progress, 0) as progress,
               COALESCE(um.claimed, false) as claimed,
               COALESCE(um.date, CURRENT_DATE) as date
        FROM gamification.daily_missions dm
        LEFT JOIN gamification.user_missions um ON dm.id = um.mission_id AND um.user_id = :userId AND um.date = CURRENT_DATE
        WHERE dm.is_active = true
        """, nativeQuery = true)
    List<MissionRow> findActiveMissionsByUserId(@Param("userId") UUID userId);
}