package com.yomu.gamification.service;

import com.yomu.gamification.dto.MissionRow;
import com.yomu.gamification.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    public List<MissionRow> getUserMissions(UUID userId) {
        return missionRepository.findActiveMissionsByUserId(userId);
    }
}