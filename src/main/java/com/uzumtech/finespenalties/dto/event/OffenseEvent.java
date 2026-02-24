package com.uzumtech.finespenalties.dto.event;

import jakarta.validation.constraints.NotNull;

public record OffenseEvent(@NotNull Long offenseId) {
}
