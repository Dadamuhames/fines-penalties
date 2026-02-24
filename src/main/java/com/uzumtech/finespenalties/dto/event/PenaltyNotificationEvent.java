package com.uzumtech.finespenalties.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PenaltyNotificationEvent(@NotNull Long penaltyId, @NotBlank String userEmail) {
}
