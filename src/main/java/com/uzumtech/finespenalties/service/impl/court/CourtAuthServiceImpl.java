package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.constant.RedisConstants;
import com.uzumtech.finespenalties.dto.request.court.CourtRefreshRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtTokenResponse;
import com.uzumtech.finespenalties.exception.http.HttpClientException;
import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtAuthServiceImpl implements CourtAuthService {
    private final CourtAdapter courtAdapter;
    private final RedisTemplate<String, Object> redisTemplate;

    public String getAuthToken() {
        String accessToken = (String) redisTemplate.opsForValue().get(RedisConstants.COURT_ACCESS_TOKEN);

        if (accessToken == null) accessToken = getNewToken();

        return accessToken;
    }

    private String getNewToken() {
        CourtTokenResponse tokenResponse = fetchTokens();

        storeTokens(tokenResponse);

        return tokenResponse.accessToken();
    }


    private CourtTokenResponse fetchTokens() {
        String refreshToken = (String) redisTemplate.opsForValue().get(RedisConstants.COURT_REFRESH_TOKEN);

        CourtTokenResponse tokenResponse;

        if (refreshToken == null) {

            tokenResponse = courtAdapter.sendLoginRequest();

        } else {

            tokenResponse = getNewPair(refreshToken);
        }

        return tokenResponse;
    }

    private CourtTokenResponse getNewPair(String refreshToken) {
        CourtRefreshRequest request = new CourtRefreshRequest(refreshToken);

        CourtTokenResponse tokenResponse;

        try {
            tokenResponse = courtAdapter.sendRefreshRequest(request);

        } catch (HttpClientException ex) {

            tokenResponse = courtAdapter.sendLoginRequest();
        }

        return tokenResponse;
    }


    public void flushTokens() {
        redisTemplate.delete(RedisConstants.COURT_ACCESS_TOKEN);
        redisTemplate.delete(RedisConstants.COURT_REFRESH_TOKEN);
    }


    private void storeTokens(CourtTokenResponse tokenResponse) {
        redisTemplate.opsForValue().set(RedisConstants.COURT_ACCESS_TOKEN, tokenResponse.accessToken(), RedisConstants.COURT_ACCESS_TOKEN_TTL);
        redisTemplate.opsForValue().set(RedisConstants.COURT_REFRESH_TOKEN, tokenResponse.refreshToken(), RedisConstants.COURT_REFRESH_TOKEN_TTL);
    }
}
