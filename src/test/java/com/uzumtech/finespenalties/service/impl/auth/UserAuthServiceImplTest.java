package com.uzumtech.finespenalties.service.impl.auth;

import com.uzumtech.finespenalties.constant.OtpConstants;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OtpVerificationLockoutService lockoutService;
    @Mock
    private OtpService otpService;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    private final String VALID_PINFL = "12345678901234";
    private final String PHONE = "998901234567";
    private final String RAW_PASSWORD = "Password123!";
    private final String ENCODED_PASSWORD = "encodedPassword";
    private final String OTP_CODE = "123456";

    private final String ACCESS_TOKEN = "access-token";
    private final String REFRESH_TOKEN = "refresh-token";

    @Test
    void loginWithPassword_ShouldSucceed_WhenCredentialsAreValid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPassword(ENCODED_PASSWORD);
        UserPasswordLoginRequest request = new UserPasswordLoginRequest(VALID_PINFL, RAW_PASSWORD);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenService.createPair(user)).thenReturn(new TokenResponse(ACCESS_TOKEN, REFRESH_TOKEN));

        // Act
        TokenResponse response = userAuthService.loginWithPassword(request);

        // Assert
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
        verify(tokenService).createPair(user);
    }

    @Test
    void loginWithPassword_ShouldThrowLoginNotFoundException_WhenUserDoesNotExist() {
        UserPasswordLoginRequest request = new UserPasswordLoginRequest(VALID_PINFL, RAW_PASSWORD);
        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.empty());

        assertThrows(LoginNotFoundException.class, () -> userAuthService.loginWithPassword(request));
    }

    @Test
    void loginWithPassword_ShouldThrowPasswordInvalidException_WhenUserHasNoPassword() {
        UserEntity user = new UserEntity();
        UserPasswordLoginRequest request = new UserPasswordLoginRequest(VALID_PINFL, RAW_PASSWORD);
        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));

        assertThrows(PasswordInvalidException.class,
            () -> userAuthService.loginWithPassword(request));
    }

    @Test
    void loginWithPassword_ShouldThrowPasswordInvalidException_WhenPasswordIsIncorrect() {
        UserEntity user = new UserEntity();

        user.setPassword(ENCODED_PASSWORD);

        UserPasswordLoginRequest request = new UserPasswordLoginRequest(VALID_PINFL, RAW_PASSWORD);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

        assertThrows(PasswordInvalidException.class,
            () -> userAuthService.loginWithPassword(request));
    }

    @Test
    void loginWithOtp_ShouldSucceed_WhenOtpIsValid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(PHONE);
        UserOtpLoginRequest request = new UserOtpLoginRequest(VALID_PINFL, OTP_CODE);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(lockoutService.isLocked(PHONE)).thenReturn(false);
        when(otpService.isOtpValid(PHONE, OTP_CODE)).thenReturn(true);
        when(tokenService.createPair(user)).thenReturn(new TokenResponse("at", "rt"));

        // Act
        TokenResponse response = userAuthService.loginWithOtp(request);

        // Assert
        assertNotNull(response);
        verify(otpService).invalidateOtp(PHONE);
        verify(tokenService).createPair(user);
    }

    @Test
    void loginWithOtp_ShouldThrowOtpCheckLockedException_WhenUserIsLockedOut() {
        UserEntity user = new UserEntity();
        user.setPhone(PHONE);
        UserOtpLoginRequest request = new UserOtpLoginRequest(VALID_PINFL, OTP_CODE);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(lockoutService.isLocked(PHONE)).thenReturn(true);

        assertThrows(OtpCheckLockedException.class, () -> userAuthService.loginWithOtp(request));
    }

    @Test
    void loginWithOtp_ShouldIncreaseAttempt_WhenOtpIsInvalid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(PHONE);
        UserOtpLoginRequest request = new UserOtpLoginRequest(VALID_PINFL, OTP_CODE);

        OtpCheckLockoutEntity lockoutEntity = new OtpCheckLockoutEntity();
        lockoutEntity.setAttempt(2);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(lockoutService.isLocked(PHONE)).thenReturn(false);
        when(otpService.isOtpValid(PHONE, OTP_CODE)).thenReturn(false);
        when(lockoutService.getInstance(PHONE)).thenReturn(lockoutEntity);

        // Act & Assert
        assertThrows(OtpInvalidException.class, () -> userAuthService.loginWithOtp(request));

        verify(lockoutService).increaseAttempt(PHONE);
        verify(lockoutService, never()).lockoutForSeconds(anyString(), anyInt());
    }

    @Test
    void loginWithOtp_ShouldTriggerLockout_WhenMaxAttemptsReached() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(PHONE);
        UserOtpLoginRequest request = new UserOtpLoginRequest(VALID_PINFL, OTP_CODE);

        OtpCheckLockoutEntity lockoutEntity = new OtpCheckLockoutEntity();
        lockoutEntity.setAttempt(OtpConstants.MAX_CHECK_ATTEMPT_COUNT_BEFORE_HOUR_LOCKOUT);

        when(userRepository.findByPinfl(VALID_PINFL)).thenReturn(Optional.of(user));
        when(lockoutService.isLocked(PHONE)).thenReturn(false);
        when(otpService.isOtpValid(PHONE, OTP_CODE)).thenReturn(false);
        when(lockoutService.getInstance(PHONE)).thenReturn(lockoutEntity);

        // Act & Assert
        assertThrows(OtpInvalidException.class, () -> userAuthService.loginWithOtp(request));
        verify(lockoutService).lockoutForSeconds(eq(PHONE), eq(OtpConstants.ONE_HOUR_IN_SECONDS));
    }
}