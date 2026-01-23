package com.uzumtech.finespenalties.service.impl.auth;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.UserLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserOtpLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserRegisterRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.OtpCheckLockedException;
import com.uzumtech.finespenalties.exception.PasswordInvalidException;
import com.uzumtech.finespenalties.exception.UserNotFoundException;
import com.uzumtech.finespenalties.mapper.UserMapper;
import com.uzumtech.finespenalties.repository.UserRepository;
import com.uzumtech.finespenalties.service.intr.OtpVerificationLockoutService;
import com.uzumtech.finespenalties.service.impl.auth.otp.OtpService;
import com.uzumtech.finespenalties.service.intr.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final OtpVerificationLockoutService verificationLockoutService;
    private final OtpService otpService;
    private final UserMapper userMapper;

    @Transactional
    public TokenResponse register(final UserRegisterRequest request) {
//        if (verificationLockoutService.isLocked(request.phone())) {
//            throw new OtpCheckLockedException(ErrorCode.OTP_CHECK_LOCKED_CODE);
//        }
//
//        otpService.validateOtp(request.phone(), request.code());
//
//        String encryptedPassword = passwordEncoder.encode(request.password());
//
//        UserEntity user = userMapper.mapRegisterRequest(request, encryptedPassword);
//
//        // TODO: verify info with GCP
//
//        user = userRepository.save(user);
//
//        return tokenService.createPair(user);

        return null;
    }

    private UserEntity getUser(final String phone) {
        return userRepository.findByPhone(phone).orElseThrow(() -> new UserNotFoundException(ErrorCode.LOGIN_INVALID_CODE));
    }

    public TokenResponse loginWithPassword(final UserLoginRequest request) {
        UserEntity user = getUser(request.phone());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new PasswordInvalidException(ErrorCode.PASSWORD_INVALID_CODE);
        }

        return tokenService.createPair(user);
    }

    public TokenResponse loginWithOtp(final UserOtpLoginRequest request) {
        if (verificationLockoutService.isLocked(request.phone())) {
            throw new OtpCheckLockedException(ErrorCode.OTP_CHECK_LOCKED_CODE);
        }

        otpService.validateOtp(request.phone(), request.otp());

        UserEntity user = getUser(request.phone());

        return tokenService.createPair(user);
    }
}
