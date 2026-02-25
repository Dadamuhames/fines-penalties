package com.uzumtech.finespenalties.service.intr.user;

import com.uzumtech.finespenalties.dto.request.UserOtpLoginRequest;
import com.uzumtech.finespenalties.dto.request.UserPasswordLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;

public interface UserAuthService {

    TokenResponse loginWithPassword(UserPasswordLoginRequest request);

    TokenResponse loginWithOtp(UserOtpLoginRequest request);



}
