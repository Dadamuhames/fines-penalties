package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserOtpLoginRequest(@NotBlank(message = "phone number required") String phone,
                                  @NotBlank(message = "OTP required") String otp) {}
