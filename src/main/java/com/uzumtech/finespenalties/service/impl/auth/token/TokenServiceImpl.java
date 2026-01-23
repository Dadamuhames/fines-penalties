package com.uzumtech.finespenalties.service.impl.auth.token;

import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.RefreshTokenEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import com.uzumtech.finespenalties.service.intr.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public TokenResponse createPair(final CustomUserDetails user) {
        String accessToken = jwtService.generateToken(user);

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }
}
