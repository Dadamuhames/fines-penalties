package com.uzumtech.finespenalties.controller.common;

import com.uzumtech.finespenalties.dto.request.RefreshRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.service.intr.token.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties")
public class CommonAuthController {

    private final TokenService tokenService;

    @PostMapping({"/inspector/auth/refresh", "/user/auth/refresh"})
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {

        TokenResponse response = tokenService.refreshToken(request);

        return ResponseEntity.ok(response);
    }
}
