package com.yomu.gamification.service;

import com.yomu.gamification.dto.*;
import com.yomu.gamification.entity.Clan;
import com.yomu.gamification.entity.ClanMember;
import com.yomu.gamification.exception.ConflictException;
import com.yomu.gamification.exception.ForbiddenException;
import com.yomu.gamification.exception.ResourceNotFoundException;
import com.yomu.gamification.repository.ClanEntityRepository;
import com.yomu.gamification.repository.ClanMemberRepository;
import com.yomu.gamification.repository.ClanRepository;
import com.yomu.gamification.repository.LeaderboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClanService {

    private final ClanEntityRepository clanEntityRepository;
    private final ClanMemberRepository clanMemberRepository;
    private final ClanRepository clanRepository;
    private final LeaderboardRepository leaderboardRepository;

    public ClanService(ClanEntityRepository clanEntityRepository,
                       ClanMemberRepository clanMemberRepository,
                       ClanRepository clanRepository,
                       LeaderboardRepository leaderboardRepository) {
        this.clanEntityRepository = clanEntityRepository;
        this.clanMemberRepository = clanMemberRepository;
        this.clanRepository = clanRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    public List<ClanRow> getAllClans(UUID userId) {
        List<Object[]> results = clanRepository.findAllClansWithUserRole(userId);
        return results.stream()
                .map(row -> new ClanRow(
                        row[0].toString(),
                        (String) row[1],
                        (String) row[2],
                        row[3] instanceof Number ? ((Number) row[3]).doubleValue() : 0.0,
                        row[4].toString(),
                        (String) row[5],
                        row[6] instanceof Number ? ((Number) row[6]).longValue() : 0L,
                        (String) row[7]
                ))
                .toList();
    }

    public Optional<ClanRow> getUserClan(UUID userId) {
        return clanRepository.findUserClanByUserId(userId);
    }

    public List<ClanLeaderboardEntry> getGlobalLeaderboard() {
        return clanRepository.findGlobalClanLeaderboard();
    }

    public List<LeaderboardEntry> getClanLeaderboard(UUID clanId) {
        return leaderboardRepository.findClanLeaderboardByClanId(clanId);
    }

    @Transactional
    public ClanDTO createClan(UUID userId, CreateClanRequest request) {
        // Check if user is already in a clan
        if (clanMemberRepository.existsByUserId(userId)) {
            throw new ConflictException("User is already in a clan");
        }

        // Check if clan name is taken
        if (clanEntityRepository.findByName(request.getName()).isPresent()) {
            throw new ConflictException("Clan name is already taken");
        }

        Clan clan = new Clan();
        clan.setName(request.getName());
        clan.setLeaderId(userId);
        clan.setTier("bronze");
        clan.setTotalScore(BigDecimal.ZERO);
        clan = clanEntityRepository.save(clan);

        ClanMember member = new ClanMember();
        member.setClanId(clan.getId());
        member.setUserId(userId);
        member.setRole("leader");
        clanMemberRepository.save(member);

        return toDTO(clan, 1, "leader");
    }

    @Transactional
    public void joinClan(UUID userId, UUID clanId) {
        // Check if user is already in a clan
        if (clanMemberRepository.existsByUserId(userId)) {
            throw new ConflictException("User is already in a clan");
        }

        Clan clan = clanEntityRepository.findById(clanId)
                .orElseThrow(() -> new ResourceNotFoundException("Clan not found"));

        ClanMember member = new ClanMember();
        member.setClanId(clanId);
        member.setUserId(userId);
        member.setRole("member");
        clanMemberRepository.save(member);
    }

    @Transactional
    public void leaveClan(UUID userId, UUID clanId) {
        ClanMember member = clanMemberRepository.findByClanIdAndUserId(clanId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this clan"));

        if ("leader".equals(member.getRole())) {
            throw new ForbiddenException("Leaders cannot leave the clan. Delete the clan instead.");
        }

        clanMemberRepository.delete(member);
    }

    @Transactional
    public void deleteClan(UUID userId, UUID clanId, boolean isAdmin) {
        Clan clan = clanEntityRepository.findById(clanId)
                .orElseThrow(() -> new ResourceNotFoundException("Clan not found"));

        // Check authorization: admin can delete any clan, leader can only delete their own
        if (!isAdmin && !clan.getLeaderId().equals(userId)) {
            throw new ForbiddenException("Only the clan leader or an admin can delete this clan");
        }

        // Delete all clan members first
        clanMemberRepository.deleteAll(clanMemberRepository.findAll().stream()
                .filter(m -> m.getClanId().equals(clanId))
                .toList());

        clanEntityRepository.delete(clan);
    }

    private ClanDTO toDTO(Clan clan, long memberCount, String myRole) {
        return new ClanDTO(
                clan.getId(),
                clan.getName(),
                clan.getTier(),
                clan.getTotalScore(),
                clan.getLeaderId(),
                memberCount,
                myRole
        );
    }
}