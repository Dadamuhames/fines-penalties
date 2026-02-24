package com.uzumtech.finespenalties.service.intr.token;

import com.uzumtech.finespenalties.entity.RefreshTokenEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;

public interface RefreshTokenService {

    RefreshTokenEntity createRefreshToken(final CustomUserDetails userDetails);

    RefreshTokenEntity findByToken(final String token);

    CustomUserDetails getUserDetails(final RefreshTokenEntity refreshToken);

    void verifyExpiration(final RefreshTokenEntity token);

    void expireToken(RefreshTokenEntity token);
}
