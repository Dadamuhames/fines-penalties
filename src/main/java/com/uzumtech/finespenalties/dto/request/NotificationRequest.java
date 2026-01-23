package com.uzumtech.finespenalties.dto.request;

import com.uzumtech.finespenalties.constant.enums.NotificationType;

public record NotificationRequest(NotificationReceiver receiver, NotificationType type, String text) {
    public record NotificationReceiver(String phone, String email, String firebaseToken) {}

    public static NotificationRequest sms(String phone, String text) {
        var receiver = new NotificationReceiver(phone, null, null);

        return new NotificationRequest(receiver, NotificationType.SMS, text);
    }
}
