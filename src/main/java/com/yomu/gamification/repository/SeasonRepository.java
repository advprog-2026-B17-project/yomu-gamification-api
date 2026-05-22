package com.yomu.gamification.repository;

import com.yomu.gamification.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeasonRepository extends JpaRepository<Season, UUID> {
    Optional<Season> findByIsActiveTrue();
}