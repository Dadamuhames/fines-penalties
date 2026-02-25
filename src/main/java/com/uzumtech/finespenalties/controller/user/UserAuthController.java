package com.uzumtech.finespenalties.controller.user;

import com.uzumtech.finespenalties.dto.request.UserOtpLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserPasswordLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.service.intr.user.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/user/auth")
public class UserAuthController {
    private final UserAuthService authService;

    @PostMapping("/login-with-otp")
    public ResponseEntity<TokenResponse> loginWithOtp(@Valid @RequestBody UserOtpLoginRequest request) {
        TokenResponse response = authService.loginWithOtp(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login-with-password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody UserPasswordLoginRequest request) {
        TokenResponse response = authService.loginWithPassword(request);

        return ResponseEntity.ok(response);
    }
}
