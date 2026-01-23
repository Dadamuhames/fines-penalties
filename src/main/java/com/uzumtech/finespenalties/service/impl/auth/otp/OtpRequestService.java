package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.constant.TimeConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import com.uzumtech.finespenalties.dto.request.OtpRequest;
import com.uzumtech.finespenalties.dto.response.OtpResponse;
import com.uzumtech.finespenalties.exception.OtpRequestLocked;
import com.uzumtech.finespenalties.service.intr.OtpRequestLockoutService;
import com.uzumtech.finespenalties.service.impl.kafka.publisher.OtpPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpRequestService {
    private final OtpService otpService;
    private final OtpRequestLockoutService lockoutService;
    private final OtpPublisherService otpPublisherService;


    public OtpResponse sendOtp(final OtpRequest request) {
        if (lockoutService.isLocked(request.phone())) {
            throw new OtpRequestLocked(ErrorCode.OTP_REQUEST_LOCKED_CODE);
        }

        OtpDto otpDto = otpService.createOtp(request.phone());

        // send event for notification service
        otpPublisherService.publish(new OtpSendEvent(request.phone(), otpDto.otp()));

        // lock otp requests
        long cooldownSeconds = TimeConstants.ONE_MINUTE_IN_SECONDS;

        if (otpDto.attempt() >= 3) cooldownSeconds = TimeConstants.ONE_HOUR_IN_SECONDS;

        lockoutService.lockoutForSeconds(request.phone(), cooldownSeconds);

        return new OtpResponse(cooldownSeconds);
    }
}
