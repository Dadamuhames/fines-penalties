package com.uzumtech.finespenalties.service.impl.auth.token;

import com.uzumtech.finespenalties.dto.request.RefreshRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;
import com.uzumtech.finespenalties.entity.RefreshTokenEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import com.uzumtech.finespenalties.service.intr.token.JwtService;
import com.uzumtech.finespenalties.service.intr.token.RefreshTokenService;
import com.uzumtech.finespenalties.service.intr.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TokenResponse refreshToken(final RefreshRequest request) {
        RefreshTokenEntity refreshToken = refreshTokenService.findByToken(request.refreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        CustomUserDetails user = refreshTokenService.getUserDetails(refreshToken);

        // delete old token
        refreshTokenService.expireToken(refreshToken);

        return createPair(user);
    }
}
