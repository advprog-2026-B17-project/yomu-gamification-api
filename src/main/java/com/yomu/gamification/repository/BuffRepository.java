package com.yomu.gamification.repository;

import com.yomu.gamification.entity.Buff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BuffRepository extends JpaRepository<Buff, UUID> {
    List<Buff> findByClanIdAndExpiresAtIsNull(UUID clanId);
}