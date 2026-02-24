package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.component.kafka.publisher.OtpPublisher;
import com.uzumtech.finespenalties.constant.TimeConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import com.uzumtech.finespenalties.dto.request.OtpByPinflRequest;
import com.uzumtech.finespenalties.dto.response.OtpResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.OtpRequestLocked;
import com.uzumtech.finespenalties.service.intr.otp.OtpRequestLockoutService;
import com.uzumtech.finespenalties.service.intr.otp.OtpRequestService;
import com.uzumtech.finespenalties.service.intr.otp.OtpService;
import com.uzumtech.finespenalties.service.intr.user.UserRegisterService;
import com.uzumtech.finespenalties.utils.PhoneUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpRequestServiceImpl implements OtpRequestService {
    private final OtpService otpService;
    private final OtpRequestLockoutService lockoutService;
    private final UserRegisterService userRegisterService;
    private final OtpPublisher otpPublisher;
    private final PhoneUtils phoneUtils;


    private OtpResponse sendOtp(final String phone) {
        if (lockoutService.isLocked(phone)) {
            throw new OtpRequestLocked(ErrorCode.OTP_REQUEST_LOCKED_CODE);
        }

        OtpDto otpDto = otpService.createOtp(phone);

        // send event for notification service
        otpPublisher.publish(new OtpSendEvent(phone, otpDto.otp()));

        // lock otp requests
        long cooldownSeconds = TimeConstants.ONE_MINUTE_IN_SECONDS;

        if (otpDto.attempt() >= 3) cooldownSeconds = TimeConstants.ONE_HOUR_IN_SECONDS;

        lockoutService.lockoutForSeconds(phone, cooldownSeconds);

        return new OtpResponse(phoneUtils.maskPhone(phone), cooldownSeconds);
    }

    public OtpResponse sendByPinfl(final OtpByPinflRequest request) {
        UserEntity user = userRegisterService.findUserByPinflOrRegister(request.pinfl());

        return sendOtp(user.getPhone());
    }
}
