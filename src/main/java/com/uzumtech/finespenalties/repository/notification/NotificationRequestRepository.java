package com.uzumtech.finespenalties.repository.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRequestRepository extends JpaRepository<NotificationRequestEntity, Long> {

    Optional<NotificationRequestEntity> findByNotificationServiceId(Long notificationServiceId);

    Optional<NotificationRequestEntity> findByRequestId(UUID requestId);

    @Modifying
    @Query("update NotificationRequestEntity n set n.requestStatus = :status, n.updatedAt = CURRENT_TIMESTAMP WHERE n.requestId = :requestId ")
    void updateRequestStatus(@Param("requestId") UUID requestId, @Param("status") NotificationRequestStatus status);

    @Modifying
    @Query("update NotificationRequestEntity n set " +
        "n.notificationServiceId = :notificationServiceId, " +
        "n.requestStatus = 'DELIVERED', " +
        "n.updatedAt = CURRENT_TIMESTAMP where n.requestId = :requestId")
    void markAsDelivered(@Param("requestId") UUID requestId, @Param("notificationServiceId") Long notificationServiceId);


    @Query("SELECT COUNT(n.id) > 0 FROM NotificationRequestEntity n " +
        "WHERE n.requestId = :requestId AND (n.requestStatus = 'NEW' OR n.requestStatus = 'SENT_TO_RETRY') ")
    boolean isAvailableForProcessing(@Param("requestId") UUID requestId);

}
