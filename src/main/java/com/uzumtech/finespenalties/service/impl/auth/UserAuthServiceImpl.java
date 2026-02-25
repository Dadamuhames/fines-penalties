package com.uzumtech.finespenalties.service.impl.auth;

import com.uzumtech.finespenalties.constant.OtpConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.UserOtpLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserPasswordLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.entity.redis.OtpCheckLockoutEntity;
import com.uzumtech.finespenalties.exception.LoginNotFoundException;
import com.uzumtech.finespenalties.exception.OtpCheckLockedException;
import com.uzumtech.finespenalties.exception.OtpInvalidException;
import com.uzumtech.finespenalties.exception.PasswordInvalidException;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.otp.OtpService;
import com.uzumtech.finespenalties.service.intr.otp.OtpVerificationLockoutService;
import com.uzumtech.finespenalties.service.intr.token.TokenService;
import com.uzumtech.finespenalties.service.intr.user.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final OtpVerificationLockoutService verificationLockoutService;
    private final OtpService otpService;

    private UserEntity getUser(String pinfl) {
        return userRepository.findByPinfl(pinfl).orElseThrow(() -> new LoginNotFoundException(ErrorCode.LOGIN_INVALID_CODE));
    }

    public TokenResponse loginWithPassword(UserPasswordLoginRequest request) {
        UserEntity user = getUser(request.pinfl());

        if (user.getPassword() == null) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_NOT_EXISTS_CODE);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(user);
    }

    public TokenResponse loginWithOtp(UserOtpLoginRequest request) {
        UserEntity user = getUser(request.pinfl());

        String phone = user.getPhone();

        if (verificationLockoutService.isLocked(phone)) {
            throw new OtpCheckLockedException(ErrorCode.OTP_CHECK_LOCKED_CODE);
        }

        validateOtpAndLockoutIfInvalid(phone, request.otp());

        return tokenService.createPair(user);
    }

    private void validateOtpAndLockoutIfInvalid(String phone, String otpCode) {
        if (!otpService.isOtpValid(phone, otpCode)) {
            OtpCheckLockoutEntity checkLockoutEntity = verificationLockoutService.getInstance(phone);

            if (checkLockoutEntity.getAttempt() >= OtpConstants.MAX_CHECK_ATTEMPT_COUNT_BEFORE_HOUR_LOCKOUT) {
                verificationLockoutService.lockoutForSeconds(phone, OtpConstants.ONE_HOUR_IN_SECONDS);
            } else {
                verificationLockoutService.increaseAttempt(phone);
            }

            throw new OtpInvalidException(ErrorCode.OTP_EXPIRED_CODE);
        }

        // if OTP is valid => invalidate it so it cannot be used again
        otpService.invalidateOtp(phone);
    }
}
