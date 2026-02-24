package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OtpByPinflRequest(@NotBlank(message = "pinfl required") @Size(min = 14, max = 14) String pinfl) {
}
