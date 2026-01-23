package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.entity.redis.OtpCheckLockoutEntity;

public interface OtpVerificationLockoutService {

    OtpCheckLockoutEntity getInstance(String phone);

    void lockoutForSeconds(String phone, long seconds);

    void increaseAttempt(String phone);

    boolean isLocked(String phone);
}
