package com.yomu.gamification.service;

import com.yomu.gamification.dto.BuffDTO;
import com.yomu.gamification.dto.ClanBuffsResponse;
import com.yomu.gamification.entity.Buff;
import com.yomu.gamification.exception.ResourceNotFoundException;
import com.yomu.gamification.repository.BuffRepository;
import com.yomu.gamification.repository.ClanEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BuffService {

    private final BuffRepository buffRepository;
    private final ClanEntityRepository clanEntityRepository;

    public BuffService(BuffRepository buffRepository, ClanEntityRepository clanEntityRepository) {
        this.buffRepository = buffRepository;
        this.clanEntityRepository = clanEntityRepository;
    }

    public ClanBuffsResponse getClanBuffs(UUID clanId) {
        // Verify clan exists
        if (!clanEntityRepository.existsById(clanId)) {
            throw new ResourceNotFoundException("Clan not found");
        }

        List<Buff> buffs = buffRepository.findByClanIdAndExpiresAtIsNull(clanId);

        List<BuffDTO> buffDTOs = buffs.stream()
                .map(this::toDTO)
                .toList();

        return new ClanBuffsResponse(clanId.toString(), buffDTOs);
    }

    private BuffDTO toDTO(Buff buff) {
        return new BuffDTO(
                buff.getBuffType(),
                buff.getMultiplier(),
                buff.getActivatedAt(),
                getBuffDescription(buff.getBuffType(), buff.getMultiplier())
        );
    }

    private String getBuffDescription(String buffType, double multiplier) {
        return switch (buffType) {
            case "productivity_buff" -> "50%+ anggota selesaikan misi harian";
            case "low_accuracy_penalty" -> "Rata-rata akurasi <50%";
            case "consistent_reader_buff" -> "Rata-rata akurasi >=80%";
            case "inactive_penalty" -> "<30% anggota aktif dalam 3 hari";
            default -> "Aktif";
        };
    }
}