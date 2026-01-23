package com.uzumtech.finespenalties.constant.enums;

import lombok.Getter;

@Getter
public enum KafkaErrorMessage {
    NOTIFICATION_EVENT_MALFORMED("Notification event malformed"),
    WEBHOOK_EVENT_MALFORMED("Webhook event malformed"),
    WEBHOOK_UNAVAILABLE("Merchant's Webhook unavailable"),
    WEBHOOK_REQUEST_INVALID("Webhook Request invalid"),
    MERCHANT_ID_INVALID("Merchant id invalid");

    private final String message;

    KafkaErrorMessage(String message) {
        this.message = message;
    }
}
