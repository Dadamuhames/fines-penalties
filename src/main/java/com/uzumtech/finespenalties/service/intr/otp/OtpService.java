package com.uzumtech.finespenalties.service.intr.otp;

import com.uzumtech.finespenalties.dto.OtpDto;

public interface OtpService {

    OtpDto createOtp(String phone);

    boolean isOtpValid(String phone, String otpCode);

    void invalidateOtp(String phone);
}
