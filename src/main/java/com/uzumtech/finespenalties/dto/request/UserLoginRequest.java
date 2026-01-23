package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@NotBlank(message = "Phone required") String phone,
                               @NotBlank(message = "Password required") String password) {}
