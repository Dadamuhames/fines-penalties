package com.uzumtech.finespenalties.service.intr.token;

import com.uzumtech.finespenalties.dto.request.RefreshRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;

public interface TokenService {
    TokenResponse createPair(CustomUserDetails user);

    TokenResponse refreshToken(RefreshRequest request);
}
