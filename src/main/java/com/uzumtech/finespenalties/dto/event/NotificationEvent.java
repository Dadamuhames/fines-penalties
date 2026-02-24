package com.uzumtech.finespenalties.dto.event;

import com.uzumtech.finespenalties.constant.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NotificationEvent(@NotNull UUID requestId, @NotBlank String text, @NotBlank String receiver, @NotNull NotificationType type) {}
