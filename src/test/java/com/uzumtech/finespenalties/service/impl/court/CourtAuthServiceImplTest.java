package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.constant.RedisConstants;
import com.uzumtech.finespenalties.dto.request.court.CourtRefreshRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtTokenResponse;
import com.uzumtech.finespenalties.exception.http.HttpClientException;
import com.uzumtech.finespenalties.exception.kafka.nontransients.CourtTokenNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtAuthServiceImplTest {

    @Mock
    private CourtAdapter courtAdapter;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private CourtAuthServiceImpl courtAuthService;

    // Shared Constants
    private static final String ACCESS_TOKEN = "access-123";
    private static final String REFRESH_TOKEN = "refresh-456";
    private static final String NEW_ACCESS_TOKEN = "new-access-789";
    private static final String NEW_REFRESH_TOKEN = "new-refresh-000";

    private CourtTokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        tokenResponse = new CourtTokenResponse(ACCESS_TOKEN, REFRESH_TOKEN);
    }

    @Test
    void getAuthToken_ShouldReturnToken_WhenExistsInRedis() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(ACCESS_TOKEN);

        // Act
        String result = courtAuthService.getAuthToken();

        // Assert
        assertEquals(ACCESS_TOKEN, result);
    }

    @Test
    void getAuthToken_ShouldThrowException_WhenTokenMissingInRedis() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(null);

        // Act & Assert
        assertThrows(CourtTokenNotExistsException.class, () -> courtAuthService.getAuthToken());
    }

    @Test
    void refreshTokens_ShouldPerformFullLogin_WhenNoRefreshTokenExists() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_REFRESH_TOKEN)).thenReturn(null);
        when(courtAdapter.sendLoginRequest()).thenReturn(tokenResponse);

        // Act
        courtAuthService.refreshTokens();

        // Assert
        verify(courtAdapter).sendLoginRequest();
        verify(courtAdapter, never()).sendRefreshRequest(any());

        // Verify
        verify(valueOperations).set(eq(RedisConstants.COURT_ACCESS_TOKEN), eq(ACCESS_TOKEN), any());
        verify(valueOperations).set(eq(RedisConstants.COURT_REFRESH_TOKEN), eq(REFRESH_TOKEN), any());
    }

    @Test
    void refreshTokens_ShouldRefresh_WhenRefreshTokenExists() {
        // Arrange
        CourtTokenResponse newPair = new CourtTokenResponse(NEW_ACCESS_TOKEN, NEW_REFRESH_TOKEN);

        when(valueOperations.get(RedisConstants.COURT_REFRESH_TOKEN)).thenReturn(REFRESH_TOKEN);
        when(courtAdapter.sendRefreshRequest(any(CourtRefreshRequest.class))).thenReturn(newPair);

        // Act
        courtAuthService.refreshTokens();

        // Assert
        verify(courtAdapter).sendRefreshRequest(argThat(req -> req.refreshToken().equals(REFRESH_TOKEN)));
        verify(courtAdapter, never()).sendLoginRequest();

        verify(valueOperations).set(eq(RedisConstants.COURT_ACCESS_TOKEN), eq(NEW_ACCESS_TOKEN), any());
        verify(valueOperations).set(eq(RedisConstants.COURT_REFRESH_TOKEN), eq(NEW_REFRESH_TOKEN), any());
    }
}