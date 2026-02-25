package com.uzumtech.finespenalties.controller.user;

import com.uzumtech.finespenalties.dto.request.UserSetPasswordRequest;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.service.intr.user.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/user/profile")
public class UserProfileController {
    private final UserProfileService profileService;

    @PostMapping("/set-password")
    public ResponseEntity<Void> setPassword(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody UserSetPasswordRequest request) {
        profileService.setUserPassword(user, request);

        return ResponseEntity.ok().build();
    }
}
