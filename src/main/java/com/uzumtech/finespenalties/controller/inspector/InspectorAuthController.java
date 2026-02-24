package com.uzumtech.finespenalties.controller.inspector;

import com.uzumtech.finespenalties.dto.request.InspectorLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.service.intr.InspectorAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/inspector/auth")
public class InspectorAuthController {

    private final InspectorAuthService inspectorAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody InspectorLoginRequest request) {

        TokenResponse response = inspectorAuthService.login(request);

        return ResponseEntity.ok(response);
    }

}
