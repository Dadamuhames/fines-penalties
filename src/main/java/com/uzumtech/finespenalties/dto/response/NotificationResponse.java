package com.uzumtech.finespenalties.dto.response;

public record NotificationResponse(NotificationResponseData data) {
    public record NotificationResponseData(Long notificationId) {}
}
