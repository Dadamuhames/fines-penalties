package com.uzumtech.finespenalties.service.impl.auth.token;

import com.uzumtech.finespenalties.configuration.property.JwtProperty;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.entity.RefreshTokenEntity;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import com.uzumtech.finespenalties.exception.RefreshTokenException;
import com.uzumtech.finespenalties.repository.RefreshTokenRepository;
import com.uzumtech.finespenalties.service.impl.auth.userdetails.UserDetailDispatcher;
import com.uzumtech.finespenalties.service.intr.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperty jwtProperty;
    private final UserDetailDispatcher detailsServiceDispatcher;

    @Transactional
    public RefreshTokenEntity createRefreshToken(CustomUserDetails userDetails) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
            .token(UUID.randomUUID().toString())
            .userRole(userDetails.getUserRole())
            .subject(userDetails.getUsername())
            .expiryDate(Instant.now().plusSeconds(jwtProperty.getRefreshTtlSeconds()))
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RefreshTokenException(ErrorCode.REFRESH_TOKEN_INVALID_CODE));
    }


    public CustomUserDetails getUserDetails(RefreshTokenEntity refreshToken) {
        return (CustomUserDetails) detailsServiceDispatcher.loadUserByLoginAndRole(refreshToken.getSubject(), refreshToken.getUserRole());
    }


    public void verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(ErrorCode.REFRESH_TOKEN_INVALID_CODE);
        }
    }

    public void expireToken(RefreshTokenEntity token) {
        token.setExpiryDate(Instant.now());
        refreshTokenRepository.save(token);
    }
}
