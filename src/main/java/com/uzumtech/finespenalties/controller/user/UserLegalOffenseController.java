package com.uzumtech.finespenalties.controller.user;

import com.uzumtech.finespenalties.dto.response.LegalOffenseDetailResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.service.intr.user.UserLegalOffenseService;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/user/legal-offenses")
public class UserLegalOffenseController {
    private final UserLegalOffenseService userLegalOffenseService;

    @GetMapping
    public ResponseEntity<Page<LegalOffenseResponse>> list(@AuthenticationPrincipal UserEntity user, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") @Max(50) Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<LegalOffenseResponse> legalOffenses = userLegalOffenseService.list(user, pageable);

        return ResponseEntity.ok(legalOffenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LegalOffenseDetailResponse> getOne(@PathVariable Long id, @AuthenticationPrincipal UserEntity user) {

        LegalOffenseDetailResponse response = userLegalOffenseService.getOne(id, user);

        return ResponseEntity.ok(response);
    }
}
