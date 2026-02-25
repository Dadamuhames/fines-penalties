package com.uzumtech.finespenalties.service.impl.auth.otp;

import com.uzumtech.finespenalties.constant.OtpConstants;
import com.uzumtech.finespenalties.dto.OtpDto;
import com.uzumtech.finespenalties.entity.redis.OtpEntity;
import com.uzumtech.finespenalties.repository.redis.OtpRepository;
import com.uzumtech.finespenalties.service.intr.otp.OtpService;
import com.uzumtech.finespenalties.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpUtils otpUtils;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public OtpDto createOtp(String phone) {
        String otp = otpUtils.generateOtp();

        OtpEntity prevOtp = otpRepository.findByPhone(phone).orElse(null);

        Integer currentAttempt = OtpConstants.DEFAULT_CHECK_ATTEMPT_COUNT;

        if (prevOtp != null) {
            currentAttempt += prevOtp.getAttempt();
            otpRepository.delete(prevOtp);
        }

        OtpEntity otpEntity = OtpEntity.builder().otpHash(passwordEncoder.encode(otp)).phone(phone).attempt(currentAttempt).build();

        otpRepository.save(otpEntity);

        return new OtpDto(otp, currentAttempt);
    }

    public boolean isOtpValid(String phone, String otpCode) {
        OtpEntity otp = otpRepository.findByPhone(phone).orElse(null);

        return isOtpValid(otp, otpCode);
    }

    private boolean isOtpValid(OtpEntity otp, String otpCode) {
        return otp != null && passwordEncoder.matches(otpCode, otp.getOtpHash());
    }

    public void invalidateOtp(String phone) {
        otpRepository.findByPhone(phone).ifPresent(otpRepository::delete);
    }
}
