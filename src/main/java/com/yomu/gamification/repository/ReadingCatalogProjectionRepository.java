package com.yomu.gamification.repository;

import com.yomu.gamification.entity.ReadingCatalogProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingCatalogProjectionRepository extends JpaRepository<ReadingCatalogProjection, UUID> {
    Optional<ReadingCatalogProjection> findByReadingId(UUID readingId);
}