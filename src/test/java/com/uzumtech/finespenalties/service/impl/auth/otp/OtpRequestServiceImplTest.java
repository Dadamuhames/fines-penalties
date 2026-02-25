package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.component.kafka.publisher.OtpPublisher;
import com.uzumtech.finespenalties.constant.OtpConstants;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import com.uzumtech.finespenalties.dto.request.OtpByPinflRequest;
import com.uzumtech.finespenalties.dto.response.OtpResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.OtpRequestLocked;
import com.uzumtech.finespenalties.service.intr.otp.OtpRequestLockoutService;
import com.uzumtech.finespenalties.service.intr.otp.OtpService;
import com.uzumtech.finespenalties.service.intr.user.UserRegisterService;
import com.uzumtech.finespenalties.utils.PhoneUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpRequestServiceImplTest {

    @Mock
    private OtpService otpService;
    @Mock
    private OtpRequestLockoutService lockoutService;
    @Mock
    private UserRegisterService userRegisterService;
    @Mock
    private OtpPublisher otpPublisher;
    @Mock
    private PhoneUtils phoneUtils;

    @InjectMocks
    private OtpRequestServiceImpl otpRequestService;

    private static final String TEST_PINFL = "12345678901234";
    private static final String TEST_PHONE = "998901234567";
    private static final String TEST_MASKED_PHONE = "99890****567";
    private static final String TEST_OTP_CODE = "555666";

    @Test
    void sendByPinfl_ShouldSucceedWithShortCooldown_WhenUnderLimit() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(TEST_PHONE);
        OtpDto otpDto = new OtpDto(TEST_OTP_CODE, 1);

        when(userRegisterService.findUserByPinflOrRegister(TEST_PINFL)).thenReturn(user);
        when(lockoutService.isLocked(TEST_PHONE)).thenReturn(false);
        when(otpService.createOtp(TEST_PHONE)).thenReturn(otpDto);
        when(phoneUtils.maskPhone(TEST_PHONE)).thenReturn(TEST_MASKED_PHONE);

        // Act
        OtpResponse response = otpRequestService.sendByPinfl(new OtpByPinflRequest(TEST_PINFL));

        // Assert
        assertEquals(TEST_MASKED_PHONE, response.phone());
        assertEquals(OtpConstants.ONE_MINUTE_IN_SECONDS, response.cooldownSeconds());

        verify(otpPublisher).publish(any(OtpSendEvent.class));
        verify(lockoutService).lockoutForSeconds(TEST_PHONE, OtpConstants.ONE_MINUTE_IN_SECONDS);
    }

    @Test
    void sendByPinfl_ShouldApplyLongCooldown_WhenRequestLimitReached() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(TEST_PHONE);

        OtpDto otpDto = new OtpDto(TEST_OTP_CODE, OtpConstants.OTP_REQUEST_LIMIT);

        when(userRegisterService.findUserByPinflOrRegister(TEST_PINFL)).thenReturn(user);
        when(lockoutService.isLocked(TEST_PHONE)).thenReturn(false);
        when(otpService.createOtp(TEST_PHONE)).thenReturn(otpDto);
        when(phoneUtils.maskPhone(TEST_PHONE)).thenReturn(TEST_MASKED_PHONE);

        // Act
        OtpResponse response = otpRequestService.sendByPinfl(new OtpByPinflRequest(TEST_PINFL));

        // Assert
        assertEquals(OtpConstants.ONE_HOUR_IN_SECONDS, response.cooldownSeconds());
        verify(lockoutService).lockoutForSeconds(TEST_PHONE, OtpConstants.ONE_HOUR_IN_SECONDS);
    }

    @Test
    void sendByPinfl_ShouldThrowException_WhenUserIsAlreadyLocked() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(TEST_PHONE);

        when(userRegisterService.findUserByPinflOrRegister(TEST_PINFL)).thenReturn(user);
        when(lockoutService.isLocked(TEST_PHONE)).thenReturn(true);

        // Act & Assert
        assertThrows(OtpRequestLocked.class,
            () -> otpRequestService.sendByPinfl(new OtpByPinflRequest(TEST_PINFL)));

        verifyNoInteractions(otpService);
        verifyNoInteractions(otpPublisher);
    }

    @Test
    void sendByPinfl_ShouldPublishCorrectEventData() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPhone(TEST_PHONE);
        OtpDto otpDto = new OtpDto(TEST_OTP_CODE, 1);

        when(userRegisterService.findUserByPinflOrRegister(TEST_PINFL)).thenReturn(user);
        when(lockoutService.isLocked(TEST_PHONE)).thenReturn(false);
        when(otpService.createOtp(TEST_PHONE)).thenReturn(otpDto);

        // Act
        otpRequestService.sendByPinfl(new OtpByPinflRequest(TEST_PINFL));

        // Assert
        ArgumentCaptor<OtpSendEvent> eventCaptor = ArgumentCaptor.forClass(OtpSendEvent.class);
        verify(otpPublisher).publish(eventCaptor.capture());

        OtpSendEvent publishedEvent = eventCaptor.getValue();
        assertEquals(TEST_PHONE, publishedEvent.phone());
        assertEquals(TEST_OTP_CODE, publishedEvent.code());
    }
}