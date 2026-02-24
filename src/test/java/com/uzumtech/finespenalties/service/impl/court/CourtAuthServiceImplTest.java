package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.constant.RedisConstants;
import com.uzumtech.finespenalties.dto.request.court.CourtRefreshRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtTokenResponse;
import com.uzumtech.finespenalties.exception.http.HttpClientException;
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

    private CourtTokenResponse mockTokenResponse;
    private CourtRefreshRequest refreshRequest;
    private final String ACCESS_TOKEN = "access-123";
    private final String REFRESH_TOKEN = "refresh-456";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        mockTokenResponse = new CourtTokenResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        refreshRequest = new CourtRefreshRequest(REFRESH_TOKEN);

    }

    @Test
    void getAuthToken_ShouldReturnExistingToken_WhenPresentInRedis() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(ACCESS_TOKEN);

        // Act
        String result = courtAuthService.getAuthToken();

        // Assert
        assertEquals(ACCESS_TOKEN, result);
        verify(valueOperations, never()).set(anyString(), any(), any());
    }

    @Test
    void getAuthToken_ShouldFetchNewTokenViaLogin_WhenRedisIsEmpty() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(null);
        when(valueOperations.get(RedisConstants.COURT_REFRESH_TOKEN)).thenReturn(null);
        when(courtAdapter.sendLoginRequest()).thenReturn(mockTokenResponse);

        // Act
        String result = courtAuthService.getAuthToken();

        // Assert
        assertEquals(ACCESS_TOKEN, result);
        verify(courtAdapter).sendLoginRequest();
        verify(valueOperations).set(eq(RedisConstants.COURT_ACCESS_TOKEN), eq(ACCESS_TOKEN), any());
        verify(valueOperations).set(eq(RedisConstants.COURT_REFRESH_TOKEN), eq(REFRESH_TOKEN), any());
    }

    @Test
    void getAuthToken_ShouldRefresh_WhenAccessTokenMissingButRefreshTokenExists() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(null);
        when(valueOperations.get(RedisConstants.COURT_REFRESH_TOKEN)).thenReturn(REFRESH_TOKEN);
        when(courtAdapter.sendRefreshRequest(any(CourtRefreshRequest.class))).thenReturn(mockTokenResponse);

        // Act
        String result = courtAuthService.getAuthToken();

        // Assert
        assertEquals(ACCESS_TOKEN, result);
        verify(courtAdapter).sendRefreshRequest(refreshRequest);
    }

    @Test
    void getAuthToken_ShouldFallbackToLogin_WhenRefreshFails() {
        // Arrange
        when(valueOperations.get(RedisConstants.COURT_ACCESS_TOKEN)).thenReturn(null);
        when(valueOperations.get(RedisConstants.COURT_REFRESH_TOKEN)).thenReturn(REFRESH_TOKEN);
        when(courtAdapter.sendRefreshRequest(any())).thenThrow(new HttpClientException("Error", HttpStatus.BAD_REQUEST));
        when(courtAdapter.sendLoginRequest()).thenReturn(mockTokenResponse);

        // Act
        String result = courtAuthService.getAuthToken();

        // Assert
        assertEquals(ACCESS_TOKEN, result);
        verify(courtAdapter).sendLoginRequest();
    }
}