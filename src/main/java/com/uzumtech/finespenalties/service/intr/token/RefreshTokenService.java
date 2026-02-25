package com.uzumtech.finespenalties.service.intr.token;

import com.uzumtech.finespenalties.entity.RefreshTokenEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;

public interface RefreshTokenService {

    RefreshTokenEntity createRefreshToken(CustomUserDetails userDetails);

    RefreshTokenEntity findByToken(String token);

    CustomUserDetails getUserDetails(RefreshTokenEntity refreshToken);

    void verifyExpiration(RefreshTokenEntity token);

    void expireToken(RefreshTokenEntity token);
}
