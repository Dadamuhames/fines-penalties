package com.uzumtech.finespenalties.service.intr.otp;

import com.uzumtech.finespenalties.entity.redis.OtpLockoutEntity;

public interface OtpRequestLockoutService {

    OtpLockoutEntity getInstance(String phone);

    void lockoutForSeconds(String phone, long seconds);

    boolean isLocked(String phone);
}
