package com.uzumtech.finespenalties.service.intr.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;

import java.util.UUID;

public interface NotificationRequestStoreService {

    boolean isAvailableForProcessing(UUID requestId);

    void createNotificationRequest(NotificationEvent event);

    void markAsDelivered(UUID requestId, final Long notificationServiceId);

    void changeStatus(UUID requestId, final NotificationRequestStatus status);
}
