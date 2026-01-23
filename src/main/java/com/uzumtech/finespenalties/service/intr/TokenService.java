package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;

public interface TokenService {
    TokenResponse createPair(final CustomUserDetails user);
}
