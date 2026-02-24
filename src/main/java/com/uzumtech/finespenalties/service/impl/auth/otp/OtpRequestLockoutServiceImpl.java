package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.entity.redis.OtpLockoutEntity;
import com.uzumtech.finespenalties.repository.redis.OtpLockoutRepository;
import com.uzumtech.finespenalties.service.intr.otp.OtpRequestLockoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
public class OtpRequestLockoutServiceImpl implements OtpRequestLockoutService {
    private final OtpLockoutRepository otpLockoutRepository;

    @Override
    public OtpLockoutEntity getInstance(String phone) {
        return otpLockoutRepository.findByPhone(phone).orElse(OtpLockoutEntity.builder().phone(phone).build());
    }

    @Override
    public void lockoutForSeconds(String phone, long seconds) {
        OtpLockoutEntity lockoutEntity = getInstance(phone);

        OffsetDateTime unlockAt = OffsetDateTime.now().plusSeconds(seconds);

        lockoutEntity.setUnlockAt(unlockAt);

        otpLockoutRepository.save(lockoutEntity);
    }

    @Override
    public boolean isLocked(String phone) {
        OtpLockoutEntity lockout = getInstance(phone);

        return lockout.getUnlockAt() != null && lockout.getUnlockAt().isAfter(OffsetDateTime.now());
    }
}
