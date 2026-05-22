package com.yomu.gamification.repository;

import com.yomu.gamification.entity.ClanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClanMemberRepository extends JpaRepository<ClanMember, UUID> {
    Optional<ClanMember> findByClanIdAndUserId(UUID clanId, UUID userId);
    long countByClanId(UUID clanId);
    boolean existsByUserId(UUID userId);
}