package com.uzumtech.finespenalties.service.intr.token;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.entity.base.CustomUserDetails;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public interface JwtService {

    String extractSubject(String jwt);

    Role extractRole(String jwt);

    Claims extractAllClaims(String jwt);

    SecretKey getSignInKey();

    String generateToken(Map<String, Object> extraClaims, final String subject);

    String generateToken(CustomUserDetails userDetails);

    boolean isTokenExpired(String jwt);

    Date extractExpiration(String jwt);
}
