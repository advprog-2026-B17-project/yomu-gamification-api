package com.yomu.gamification.service;

import com.yomu.gamification.dto.NotificationRow;
import com.yomu.gamification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationRow> getUserNotifications(UUID userId) {
        List<Object[]> results = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return results.stream()
                .map(row -> new NotificationRow(
                        row[0].toString(),
                        row[1].toString(),
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        row[5] != null ? (Boolean) row[5] : false,
                        row[6] != null ? OffsetDateTime.parse(row[6].toString().substring(0, 10) + "T00:00:00Z") : null
                ))
                .toList();
    }

    public Long getUnreadCount(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public boolean markNotificationAsRead(UUID notificationId, UUID userId) {
        int updated = notificationRepository.markAsRead(notificationId, userId);
        return updated > 0;
    }
}