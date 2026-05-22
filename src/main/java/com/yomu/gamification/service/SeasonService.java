package com.yomu.gamification.service;

import com.yomu.gamification.dto.CreateSeasonRequest;
import com.yomu.gamification.dto.SeasonDTO;
import com.yomu.gamification.entity.Clan;
import com.yomu.gamification.entity.Season;
import com.yomu.gamification.exception.ResourceNotFoundException;
import com.yomu.gamification.repository.ClanEntityRepository;
import com.yomu.gamification.repository.SeasonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final ClanEntityRepository clanEntityRepository;

    public SeasonService(SeasonRepository seasonRepository, ClanEntityRepository clanEntityRepository) {
        this.seasonRepository = seasonRepository;
        this.clanEntityRepository = clanEntityRepository;
    }

    public List<SeasonDTO> getAllSeasons() {
        return seasonRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<SeasonDTO> getActiveSeason() {
        return seasonRepository.findByIsActiveTrue().map(this::toDTO);
    }

    @Transactional
    public SeasonDTO createSeason(CreateSeasonRequest request) {
        // Deactivate any currently active season
        seasonRepository.findByIsActiveTrue().ifPresent(currentSeason -> {
            currentSeason.setActive(false);
            currentSeason.setEndDate(OffsetDateTime.now());
            seasonRepository.save(currentSeason);
        });

        Season season = new Season();
        season.setName(request.getName());
        season.setStartDate(request.getStartDate() != null ? request.getStartDate() : OffsetDateTime.now());
        if (request.getEndDate() != null) {
            season.setEndDate(request.getEndDate());
        }
        season.setActive(true);
        season = seasonRepository.save(season);

        return toDTO(season);
    }

    @Transactional
    public Optional<SeasonDTO> endSeason(UUID seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found"));

        season.setActive(false);
        season.setEndDate(OffsetDateTime.now());
        season = seasonRepository.save(season);

        // Calculate final rankings and tier changes
        calculateFinalRankings();

        // Auto-create new season
        Season newSeason = new Season();
        newSeason.setName(generateNextSeasonName());
        newSeason.setStartDate(OffsetDateTime.now());
        newSeason.setActive(true);
        seasonRepository.save(newSeason);

        return Optional.of(toDTO(season));
    }

    private void calculateFinalRankings() {
        List<Clan> clans = clanEntityRepository.findAllByOrderByTotalScoreDesc();
        List<Map<String, Object>> rankings = new ArrayList<>();

        for (int i = 0; i < clans.size(); i++) {
            Clan clan = clans.get(i);
            String newTier = calculateTier(i + 1, clans.size());

            Map<String, Object> entry = new HashMap<>();
            entry.put("clanId", clan.getId().toString());
            entry.put("clanName", clan.getName());
            entry.put("totalScore", clan.getTotalScore().doubleValue());
            entry.put("newTier", newTier);
            rankings.add(entry);

            // Update clan tier immediately
            clan.setTier(newTier);
            clanEntityRepository.save(clan);
        }
    }

    private String calculateTier(int rank, int totalClans) {
        if (totalClans == 0) return "bronze";

        double percentile = (double) rank / totalClans;

        if (percentile <= 0.1) {
            return "diamond";
        } else if (percentile <= 0.3) {
            return "gold";
        } else if (percentile <= 0.6) {
            return "silver";
        } else {
            return "bronze";
        }
    }

    private String generateNextSeasonName() {
        long seasonCount = seasonRepository.count();
        return "Season " + (seasonCount + 1);
    }

    private SeasonDTO toDTO(Season season) {
        return new SeasonDTO(
            season.getId(),
            season.getName(),
            season.getStartDate(),
            season.getEndDate(),
            season.isActive()
        );
    }
}