package com.uzumtech.finespenalties.service.intr.notification;

import com.uzumtech.finespenalties.dto.request.NotificationCallbackRequest;

public interface NotificationCallbackStoreService {

    void receiveNotificationWebhook(NotificationCallbackRequest callbackRequest);
}
