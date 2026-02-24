package com.uzumtech.finespenalties.service.impl.auth.token;

import com.uzumtech.finespenalties.configuration.property.JwtProperty;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import com.uzumtech.finespenalties.exception.JwtMalformedException;
import com.uzumtech.finespenalties.service.intr.token.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtProperty jwtProperty;

    public String extractSubject(final String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public Role extractRole(final String jwt) {
        final Claims claims = extractAllClaims(jwt);

        String provider = claims.get("role").toString();

        return Role.valueOf(provider);
    }

    private <T> T extractClaim(final String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(final String jwt) {
        try {
            return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwt).getPayload();
        } catch (JwtException ex) {
            throw new JwtMalformedException(ErrorCode.JWT_INVALID_CODE, ex);
        }
    }

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperty.getSecretKey());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(final Map<String, Object> extraClaims, final String subject) {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(subject)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtProperty.getAccessTtl()))
            .signWith(getSignInKey())
            .compact();
    }

    public String generateToken(final CustomUserDetails userDetails) {
        return generateToken(Map.of("role", userDetails.getUserRole()), userDetails.getUsername());
    }

    public boolean isTokenExpired(final String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    public Date extractExpiration(final String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }
}
