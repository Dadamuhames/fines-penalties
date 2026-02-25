package com.uzumtech.finespenalties.service.impl.auth.otp;


import com.uzumtech.finespenalties.constant.OtpConstants;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.entity.redis.OtpEntity;
import com.uzumtech.finespenalties.repository.redis.OtpRepository;
import com.uzumtech.finespenalties.utils.OtpUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @Mock
    private OtpUtils otpUtils;
    @Mock
    private OtpRepository otpRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OtpServiceImpl otpService;

    private static final String TEST_PHONE = "998901234567";
    private static final String TEST_OTP_CODE = "123456";
    private static final String ENCODED_OTP = "encoded_otp_hash";

    @Test
    void createOtp_ShouldCreateNewOtp_WhenNoPreviousOtpExists() {
        // Arrange
        when(otpUtils.generateOtp()).thenReturn(TEST_OTP_CODE);
        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_OTP_CODE)).thenReturn(ENCODED_OTP);

        // Act
        OtpDto result = otpService.createOtp(TEST_PHONE);

        // Assert
        assertEquals(TEST_OTP_CODE, result.otp());
        assertEquals(OtpConstants.DEFAULT_CHECK_ATTEMPT_COUNT, result.attempt());
        verify(otpRepository).save(any(OtpEntity.class));
        verify(otpRepository, never()).delete(any());
    }

    @Test
    void createOtp_ShouldIncrementAttemptAndDeleteOldOtp_WhenPreviousOtpExists() {
        // Arrange
        int previousAttempts = 2;
        OtpEntity existingOtp = OtpEntity.builder()
            .phone(TEST_PHONE)
            .attempt(previousAttempts)
            .build();

        when(otpUtils.generateOtp()).thenReturn(TEST_OTP_CODE);
        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.of(existingOtp));
        when(passwordEncoder.encode(TEST_OTP_CODE)).thenReturn(ENCODED_OTP);

        // Act
        OtpDto result = otpService.createOtp(TEST_PHONE);

        // Assert
        int expectedAttempts = previousAttempts + OtpConstants.DEFAULT_CHECK_ATTEMPT_COUNT;
        assertEquals(expectedAttempts, result.attempt());
        verify(otpRepository).delete(existingOtp);
        verify(otpRepository).save(argThat(entity -> entity.getAttempt() == expectedAttempts));
    }


    @Test
    void isOtpValid_ShouldReturnTrue_WhenOtpMatches() {
        // Arrange
        OtpEntity otpEntity = OtpEntity.builder()
            .otpHash(ENCODED_OTP)
            .build();

        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.of(otpEntity));
        when(passwordEncoder.matches(TEST_OTP_CODE, ENCODED_OTP)).thenReturn(true);

        // Act
        boolean isValid = otpService.isOtpValid(TEST_PHONE, TEST_OTP_CODE);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isOtpValid_ShouldReturnFalse_WhenOtpNotFound() {
        // Arrange
        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.empty());

        // Act
        boolean isValid = otpService.isOtpValid(TEST_PHONE, TEST_OTP_CODE);

        // Assert
        assertFalse(isValid);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void isOtpValid_ShouldReturnFalse_WhenOtpDoesNotMatch() {
        // Arrange
        OtpEntity otpEntity = OtpEntity.builder().otpHash(ENCODED_OTP).build();

        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.of(otpEntity));
        when(passwordEncoder.matches(TEST_OTP_CODE, ENCODED_OTP)).thenReturn(false);

        // Act
        boolean isValid = otpService.isOtpValid(TEST_PHONE, TEST_OTP_CODE);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void invalidateOtp_ShouldDelete_WhenOtpExists() {
        // Arrange
        OtpEntity existingOtp = new OtpEntity();
        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.of(existingOtp));

        // Act
        otpService.invalidateOtp(TEST_PHONE);

        // Assert
        verify(otpRepository).delete(existingOtp);
    }

    @Test
    void invalidateOtp_ShouldDoNothing_WhenOtpDoesNotExist() {
        // Arrange
        when(otpRepository.findByPhone(TEST_PHONE)).thenReturn(Optional.empty());

        // Act
        otpService.invalidateOtp(TEST_PHONE);

        // Assert
        verify(otpRepository, never()).delete(any());
    }
}