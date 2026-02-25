package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.constant.RedisConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.court.CourtRefreshRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtTokenResponse;
import com.uzumtech.finespenalties.exception.kafka.nontransients.CourtTokenNotExistsException;
import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtAuthServiceImpl implements CourtAuthService {
    private final CourtAdapter courtAdapter;
    private final RedisTemplate<String, Object> redisTemplate;

    public void refreshTokens() {
        CourtTokenResponse response = fetchTokens();

        storeTokens(response);
    }

    public String getAuthToken() {
        String accessToken = (String) redisTemplate.opsForValue().get(RedisConstants.COURT_ACCESS_TOKEN);

        if (accessToken == null) throw new CourtTokenNotExistsException(ErrorCode.COURT_TOKEN_NOT_EXISTS_CODE);

        return accessToken;
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

        return courtAdapter.sendRefreshRequest(request);
    }


    private void storeTokens(CourtTokenResponse tokenResponse) {
        redisTemplate.opsForValue().set(RedisConstants.COURT_ACCESS_TOKEN, tokenResponse.accessToken(), RedisConstants.COURT_ACCESS_TOKEN_TTL);
        redisTemplate.opsForValue().set(RedisConstants.COURT_REFRESH_TOKEN, tokenResponse.refreshToken(), RedisConstants.COURT_REFRESH_TOKEN_TTL);
    }
}
