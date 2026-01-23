package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.constant.TimeConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.entity.redis.OtpCheckLockoutEntity;
import com.uzumtech.finespenalties.entity.redis.OtpEntity;
import com.uzumtech.finespenalties.exception.OtpInvalidException;
import com.uzumtech.finespenalties.repository.redis.OtpRepository;
import com.uzumtech.finespenalties.service.intr.OtpVerificationLockoutService;
import com.uzumtech.finespenalties.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpUtils otpUtils;
    private final OtpVerificationLockoutService verificationLockoutService;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public OtpDto createOtp(final String phone) {
        String otp = otpUtils.generateOtp();

        OtpEntity prevOtp = otpRepository.findByPhone(phone).orElse(null);

        Integer currentAttempt = 1;

        if (prevOtp != null) {
            currentAttempt += prevOtp.getAttempt();
            otpRepository.delete(prevOtp);
        }

        OtpEntity otpEntity = OtpEntity.builder().otpHash(passwordEncoder.encode(otp)).phone(phone).attempt(currentAttempt).build();

        otpRepository.save(otpEntity);

        return new OtpDto(otp, currentAttempt);
    }

    public void validateOtp(final String phone, final String otpCode) {
        OtpEntity otp = otpRepository.findByPhone(phone).orElse(null);

        if (!isOtpValid(otp, otpCode)) {
            OtpCheckLockoutEntity checkLockoutEntity = verificationLockoutService.getInstance(phone);

            if (checkLockoutEntity.getAttempt() >= 5) {
                verificationLockoutService.lockoutForSeconds(phone, TimeConstants.ONE_HOUR_IN_SECONDS);
            } else {
                verificationLockoutService.increaseAttempt(phone);
            }

            throw new OtpInvalidException(ErrorCode.OTP_EXPIRED_CODE);
        }
    }

    public boolean isOtpValid(final OtpEntity otp, final String otpCode) {
        return otp != null && passwordEncoder.matches(otpCode, otp.getOtpHash());
    }
}
