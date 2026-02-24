package com.uzumtech.finespenalties.service.intr.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;

import java.util.UUID;

public interface NotificationRequestStoreService {

    boolean isAvailableForProcessing(final UUID requestId);

    void createNotificationRequest(final NotificationEvent event);

    void markAsDelivered(final UUID requestId, final Long notificationServiceId);

    void changeStatus(final UUID requestId, final NotificationRequestStatus status);
}
