package com.uzumtech.finespenalties.dto.event;

import com.uzumtech.finespenalties.validation.PhoneNumberConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpSendEvent(@NotNull @PhoneNumberConstraint String phone, @NotBlank String code) {}
