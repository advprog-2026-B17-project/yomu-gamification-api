package com.yomu.gamification.repository;

import com.yomu.gamification.dto.NotificationRow;
import com.yomu.gamification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query(value = """
        SELECT id::text as id, user_id::text as user_id, notification_type, title, message, is_read, created_at
        FROM gamification.notifications
        WHERE user_id = :userId
        ORDER BY created_at DESC
        LIMIT 50
        """, nativeQuery = true)
    List<NotificationRow> findByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    @Query(value = """
        SELECT COUNT(*) FROM gamification.notifications WHERE user_id = :userId AND is_read = false
        """, nativeQuery = true)
    Long countUnreadByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query(value = """
        UPDATE gamification.notifications SET is_read = true WHERE id = :notificationId AND user_id = :userId
        """, nativeQuery = true)
    int markAsRead(@Param("notificationId") UUID notificationId, @Param("userId") UUID userId);
}