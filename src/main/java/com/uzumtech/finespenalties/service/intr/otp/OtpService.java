package com.uzumtech.finespenalties.service.intr.otp;

import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.entity.redis.OtpEntity;

public interface OtpService {

    OtpDto createOtp(final String phone);

    void validateOtp(final String phone, final String otpCode);

    boolean isOtpValid(final OtpEntity otp, final String otpCode);

    void invalidateOtp(final String phone);
}
