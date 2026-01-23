package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.dto.request.NotificationRequest;
import com.uzumtech.finespenalties.dto.response.NotificationResponse;

public interface NotificationService {

    NotificationResponse sendOtpNotification(final NotificationRequest request);
}
