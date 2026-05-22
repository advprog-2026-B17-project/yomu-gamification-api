package com.yomu.gamification.repository;

import com.yomu.gamification.dto.ClanRow;
import com.yomu.gamification.dto.ClanLeaderboardEntry;
import com.yomu.gamification.entity.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClanRepository extends JpaRepository<Clan, UUID> {

    @Query(value = """
        SELECT CAST(c.id AS text) as id, c.name, c.tier,
               CAST((COALESCE(c.total_score, 0) * COALESCE(EXP(SUM(LN(b.multiplier)) FILTER (WHERE b.expires_at IS NULL)), 1.0)) AS double precision) as total_score,
               CAST(c.leader_id AS text) as leader_id,
               COALESCE(u.display_name, u.username, 'Unknown') as leader_name,
               CAST(COUNT(DISTINCT cm.id) AS bigint) as member_count,
               MAX(CASE WHEN cm.user_id = :userId THEN cm.role ELSE NULL END) as my_role
        FROM gamification.clans c
        LEFT JOIN auth.users u ON c.leader_id = u.id
        LEFT JOIN gamification.clan_members cm ON c.id = cm.clan_id
        LEFT JOIN gamification.buffs b ON c.id = b.clan_id
        GROUP BY c.id, c.name, c.tier, c.total_score, c.leader_id, u.display_name, u.username
        ORDER BY total_score DESC, c.name ASC
        """, nativeQuery = true)
    List<ClanRow> findAllClansWithUserRole(@Param("userId") UUID userId);

    @Query(value = """
        SELECT CAST(c.id AS text) as id, c.name, c.tier, CAST(COALESCE(c.total_score, 0) AS double precision) as total_score,
               CAST(c.leader_id AS text) as leader_id,
               COALESCE(u.display_name, u.username, 'Unknown') as leader_name,
               CAST(COUNT(all_members.id) AS bigint) as member_count,
               member.role as my_role
        FROM gamification.clan_members member
        JOIN gamification.clans c ON member.clan_id = c.id
        LEFT JOIN auth.users u ON c.leader_id = u.id
        LEFT JOIN gamification.clan_members all_members ON c.id = all_members.clan_id
        WHERE member.user_id = :userId
        GROUP BY c.id, c.name, c.tier, c.total_score, c.leader_id, u.display_name, u.username, member.role
        LIMIT 1
        """, nativeQuery = true)
    Optional<ClanRow> findUserClanByUserId(@Param("userId") UUID userId);

    @Query(value = """
        SELECT CAST(c.id AS text) as clan_id, c.name as clan_name, c.tier,
               CAST(COALESCE(c.total_score, 0) AS double precision) as total_score,
               CAST(COUNT(DISTINCT cm.id) AS bigint) as member_count,
               CAST(COALESCE(EXP(SUM(LN(b.multiplier)) FILTER (WHERE b.expires_at IS NULL)), 1.0) AS double precision) as multiplier,
               CAST((COALESCE(c.total_score, 0) * COALESCE(EXP(SUM(LN(b.multiplier)) FILTER (WHERE b.expires_at IS NULL)), 1.0)) AS double precision) as effective_score
        FROM gamification.clans c
        LEFT JOIN gamification.clan_members cm ON c.id = cm.clan_id
        LEFT JOIN gamification.buffs b ON c.id = b.clan_id
        GROUP BY c.id, c.name, c.tier, c.total_score
        ORDER BY effective_score DESC, c.name ASC
        """, nativeQuery = true)
    List<ClanLeaderboardEntry> findGlobalClanLeaderboard();
}