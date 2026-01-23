package com.uzumtech.finespenalties.constant.enums;


import lombok.Getter;

@Getter
public enum WebhookRequestCodes {
    SUCCESS(200, "Success"),
    SERVICE_NOT_RESPONDING(400, "Service failure"),
    SERVICE_UNAVAILABLE(500, "Service unavailable, try again later");

    private final Integer code;
    private final String message;

    WebhookRequestCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
