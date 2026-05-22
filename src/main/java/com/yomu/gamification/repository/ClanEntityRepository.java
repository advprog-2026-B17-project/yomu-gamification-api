package com.yomu.gamification.repository;

import com.yomu.gamification.entity.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClanEntityRepository extends JpaRepository<Clan, UUID> {
    Optional<Clan> findByName(String name);
    List<Clan> findAllByOrderByTotalScoreDesc();
}