package com.uzumtech.finespenalties.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record UserSetPasswordRequest(
    @NotBlank(message = "password required") String password,
    @NotBlank(message = "passwordConfirm required") String passwordConfirm) {


    @AssertTrue(message = "passwords should match")
    private boolean isPasswordsMatch() {
        return this.password.equals(this.passwordConfirm);
    }
}
