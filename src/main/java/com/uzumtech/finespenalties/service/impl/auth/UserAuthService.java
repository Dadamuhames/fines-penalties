package com.uzumtech.finespenalties.service.impl.auth;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.UserOtpLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserPasswordLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.LoginNotFoundException;
import com.uzumtech.finespenalties.exception.OtpCheckLockedException;
import com.uzumtech.finespenalties.exception.PasswordInvalidException;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.otp.OtpService;
import com.uzumtech.finespenalties.service.intr.otp.OtpVerificationLockoutService;
import com.uzumtech.finespenalties.service.intr.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final OtpVerificationLockoutService verificationLockoutService;
    private final OtpService otpService;

    private UserEntity getUser(final String pinfl) {
        return userRepository.findByPinfl(pinfl).orElseThrow(() -> new LoginNotFoundException(ErrorCode.LOGIN_INVALID_CODE));
    }

    public TokenResponse loginWithPassword(final UserPasswordLoginRequest request) {
        UserEntity user = getUser(request.pinfl());

        if (user.getPassword() == null) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_NOT_EXISTS_CODE);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(user);
    }

    public TokenResponse loginWithOtp(final UserOtpLoginRequest request) {
        UserEntity user = getUser(request.pinfl());

        if (verificationLockoutService.isLocked(user.getPhone())) {
            throw new OtpCheckLockedException(ErrorCode.OTP_CHECK_LOCKED_CODE);
        }

        String phone = user.getPhone();

        otpService.validateOtp(phone, request.otp());

        otpService.invalidateOtp(phone);

        return tokenService.createPair(user);
    }
}
