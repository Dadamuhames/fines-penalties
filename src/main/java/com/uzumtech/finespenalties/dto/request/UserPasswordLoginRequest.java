package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordLoginRequest(@NotBlank(message = "Pinfl required") @Size(min = 14, max = 14) String pinfl,
                                       @NotBlank(message = "Password required") String password) {}
