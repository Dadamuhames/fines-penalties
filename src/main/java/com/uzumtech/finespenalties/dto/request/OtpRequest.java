package com.uzumtech.finespenalties.dto.request;

import com.uzumtech.finespenalties.validation.PhoneNumberConstraint;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(@NotNull @PhoneNumberConstraint String phone) {
}
