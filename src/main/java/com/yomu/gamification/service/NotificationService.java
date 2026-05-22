package com.yomu.gamification.service;

import com.yomu.gamification.dto.NotificationRow;
import com.yomu.gamification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
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