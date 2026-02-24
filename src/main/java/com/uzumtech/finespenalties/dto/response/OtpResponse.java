package com.uzumtech.finespenalties.dto.response;

public record OtpResponse(String phone, long cooldownSeconds) {
}
