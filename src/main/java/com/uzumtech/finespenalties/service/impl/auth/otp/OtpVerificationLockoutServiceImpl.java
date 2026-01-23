package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.entity.redis.OtpCheckLockoutEntity;
import com.uzumtech.finespenalties.repository.OtpCheckLockoutRepository;
import com.uzumtech.finespenalties.service.intr.OtpVerificationLockoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
public class OtpVerificationLockoutServiceImpl implements OtpVerificationLockoutService {
    private final OtpCheckLockoutRepository checkLockoutRepository;

    @Override
    public OtpCheckLockoutEntity getInstance(String phone) {
        return checkLockoutRepository.findByPhone(phone).orElse(OtpCheckLockoutEntity.builder().phone(phone).attempt(1).build());
    }

    @Override
    public void lockoutForSeconds(String phone, long seconds) {
        OtpCheckLockoutEntity checkLockoutEntity = getInstance(phone);

        OffsetDateTime unlockAt = OffsetDateTime.now().plusSeconds(seconds);

        checkLockoutEntity.setUnblockAt(unlockAt);

        checkLockoutRepository.save(checkLockoutEntity);
    }

    @Override
    public void increaseAttempt(String phone) {
        OtpCheckLockoutEntity checkLockoutEntity = getInstance(phone);

        checkLockoutEntity.setAttempt(checkLockoutEntity.getAttempt() + 1);

        checkLockoutRepository.save(checkLockoutEntity);
    }

    @Override
    public boolean isLocked(String phone) {
        OtpCheckLockoutEntity checkLockoutEntity = getInstance(phone);

        var now = OffsetDateTime.now();

        return checkLockoutEntity.getUnblockAt() != null && checkLockoutEntity.getUnblockAt().isAfter(now);
    }
}
