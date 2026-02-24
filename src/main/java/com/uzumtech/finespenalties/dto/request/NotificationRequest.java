package com.uzumtech.finespenalties.dto.request;

import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;

public record NotificationRequest(NotificationReceiver receiver, NotificationType type, String text) {
    public record NotificationReceiver(String phone, String email, String firebaseToken) {}

    public static NotificationRequest sms(String phone, String text) {
        var receiver = new NotificationReceiver(phone, null, null);

        return new NotificationRequest(receiver, NotificationType.SMS, text);
    }

    public static NotificationRequest email(String email, String text) {
        var receiver = new NotificationReceiver(null, email, null);

        return new NotificationRequest(receiver, NotificationType.EMAIL, text);
    }

    public static NotificationRequest push(String firebaseToken, String text) {
        var receiver = new NotificationReceiver(null, null, firebaseToken);

        return new NotificationRequest(receiver, NotificationType.PUSH, text);
    }

    public static NotificationRequest of(NotificationEvent event) {
        return switch (event.type()) {
            case SMS -> NotificationRequest.sms(event.receiver(), event.text());
            case EMAIL -> NotificationRequest.email(event.receiver(), event.text());
            case PUSH -> NotificationRequest.push(event.receiver(), event.text());
        };
    }
}
