package com.yomu.gamification.repository;

import com.yomu.gamification.dto.LeaderboardEntry;
import com.yomu.gamification.entity.ClanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaderboardRepository extends JpaRepository<ClanMember, UUID> {

    @Query(value = """
        SELECT u.id::text as user_id, u.display_name, u.username,
               COALESCE(SUM(cr.score), 0) as total_score,
               COUNT(cr.id) as readings_completed,
               COALESCE(AVG(cr.accuracy), 0)::float8 as avg_accuracy
        FROM gamification.clan_members cm
        JOIN auth.users u ON cm.user_id = u.id
        LEFT JOIN quiz.completed_readings cr ON u.id = cr.user_id
        WHERE cm.clan_id = :clanId
        GROUP BY u.id, u.display_name, u.username
        ORDER BY total_score DESC
        """, nativeQuery = true)
    List<LeaderboardEntry> findClanLeaderboardByClanId(@Param("clanId") UUID clanId);
}