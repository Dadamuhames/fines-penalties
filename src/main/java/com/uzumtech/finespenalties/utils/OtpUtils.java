package com.uzumtech.finespenalties.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class OtpUtils {
  private final SecureRandom secureRandom;

  public String generateOtp() {
    final int MIN = 10000;
    final int MAX = 99999;

    int otp = secureRandom.nextInt(MAX) + MIN;

    return String.valueOf(otp);
  }
}
