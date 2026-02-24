package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank(message = "refresh token required") String refreshToken) {
}
