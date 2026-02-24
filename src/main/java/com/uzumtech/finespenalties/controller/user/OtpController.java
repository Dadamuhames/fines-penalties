package com.uzumtech.finespenalties.controller.user;

import com.uzumtech.finespenalties.dto.request.OtpByPinflRequest;
import com.uzumtech.finespenalties.dto.response.OtpResponse;
import com.uzumtech.finespenalties.service.intr.otp.OtpRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/user/otp")
public class OtpController {
    private final OtpRequestService otpRequestService;

    @PostMapping("/requesting-by-pinfl")
    public ResponseEntity<OtpResponse> requestOtp(@Valid @RequestBody final OtpByPinflRequest request) {
        OtpResponse otpResponse = otpRequestService.sendByPinfl(request);

        return ResponseEntity.ok(otpResponse);
    }
}
