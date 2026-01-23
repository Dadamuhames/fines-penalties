package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(@NotBlank(message = "pinfl required") @Size(max = 14, min = 14) String pinfl,
                                  @NotBlank(message = "sms code required to verify phone number") @Size(min = 5, max = 5) String code,
                                  @NotBlank(message = "password required") String password,
                                  @NotBlank(message = "password confirmation required") String passwordConfirm) {


    @AssertTrue(message = "passwords should match")
    private boolean isPasswordsMatch() {
        return this.password.equals(this.passwordConfirm);
    }
}
