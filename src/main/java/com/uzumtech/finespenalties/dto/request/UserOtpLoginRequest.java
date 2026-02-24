package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserOtpLoginRequest(@NotBlank(message = "pinfl number required") @Size(max = 14, min = 14) String pinfl,
                                  @NotBlank(message = "OTP required") String otp) {}
