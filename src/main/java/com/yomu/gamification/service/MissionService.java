package com.yomu.gamification.service;

import com.yomu.gamification.dto.*;
import com.yomu.gamification.entity.DailyMission;
import com.yomu.gamification.entity.UserMission;
import com.yomu.gamification.exception.BadRequestException;
import com.yomu.gamification.exception.ResourceNotFoundException;
import com.yomu.gamification.repository.DailyMissionRepository;
import com.yomu.gamification.repository.MissionRepository;
import com.yomu.gamification.repository.UserMissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MissionService {

    private final DailyMissionRepository dailyMissionRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    public MissionService(DailyMissionRepository dailyMissionRepository,
                          MissionRepository missionRepository,
                          UserMissionRepository userMissionRepository) {
        this.dailyMissionRepository = dailyMissionRepository;
        this.missionRepository = missionRepository;
        this.userMissionRepository = userMissionRepository;
    }

    public List<MissionRow> getUserMissions(UUID userId) {
        List<Object[]> results = missionRepository.findActiveMissionsByUserId(userId);
        return results.stream()
                .map(row -> new MissionRow(
                        row[0].toString(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        row[4] instanceof Number ? ((Number) row[4]).intValue() : 0,
                        row[5] instanceof Number ? ((Number) row[5]).intValue() : 0,
                        row[6] instanceof Number ? ((Number) row[6]).intValue() : 0,
                        row[7] instanceof Boolean ? (Boolean) row[7] : false,
                        row[8] != null ? java.time.LocalDate.parse(row[8].toString().substring(0, 10)) : null
                ))
                .toList();
    }

    public List<DailyMissionDTO> getAllMissions() {
        return dailyMissionRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<DailyMissionDTO> getActiveMissions() {
        return dailyMissionRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public DailyMissionDTO createMission(CreateMissionRequest request) {
        DailyMission mission = new DailyMission();
        mission.setTitle(request.getTitle());
        mission.setDescription(request.getDescription());
        mission.setTargetType(request.getTargetType());
        mission.setTargetCount(request.getTargetCount());
        mission.setXpReward(request.getXpReward() != null ? request.getXpReward() : 10);
        mission.setIsActive(true);
        mission = dailyMissionRepository.save(mission);
        return toDTO(mission);
    }

    @Transactional
    public DailyMissionDTO updateMission(UUID id, CreateMissionRequest request) {
        DailyMission mission = dailyMissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));
        mission.setTitle(request.getTitle());
        mission.setDescription(request.getDescription());
        mission.setTargetType(request.getTargetType());
        mission.setTargetCount(request.getTargetCount());
        if (request.getXpReward() != null) {
            mission.setXpReward(request.getXpReward());
        }
        mission = dailyMissionRepository.save(mission);
        return toDTO(mission);
    }

    @Transactional
    public void deleteMission(UUID id) {
        if (!dailyMissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mission not found");
        }
        dailyMissionRepository.deleteById(id);
    }

    @Transactional
    public DailyMissionDTO toggleMissionActive(UUID id) {
        DailyMission mission = dailyMissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));
        mission.setIsActive(!mission.getIsActive());
        mission = dailyMissionRepository.save(mission);
        return toDTO(mission);
    }

    @Transactional
    public UserMissionDTO claimMission(UUID userId, UUID missionId) {
        DailyMission mission = dailyMissionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

        UserMission userMission = userMissionRepository
                .findByUserIdAndMissionIdAndDate(userId, missionId, LocalDate.now())
                .orElseThrow(() -> new BadRequestException("Mission has no progress today"));

        if (Boolean.TRUE.equals(userMission.getClaimed())) {
            throw new BadRequestException("Mission reward already claimed");
        }
        if (userMission.getProgress() < mission.getTargetCount()) {
            throw new BadRequestException("Mission is not complete");
        }

        userMission.setClaimed(true);
        userMission = userMissionRepository.save(userMission);

        return new UserMissionDTO(
                userMission.getId(),
                userMission.getMissionId(),
                userMission.getProgress(),
                mission.getTargetCount(),
                mission.getXpReward(),
                userMission.getClaimed(),
                userMission.getDate()
        );
    }

    private DailyMissionDTO toDTO(DailyMission mission) {
        return new DailyMissionDTO(
                mission.getId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getTargetType(),
                mission.getTargetCount(),
                mission.getXpReward(),
                mission.getIsActive()
        );
    }
}