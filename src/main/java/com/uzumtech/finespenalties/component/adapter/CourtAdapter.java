package com.uzumtech.finespenalties.component.adapter;

import com.uzumtech.finespenalties.configuration.property.service.CourtProperties;
import com.uzumtech.finespenalties.dto.request.court.CourtAuthRequest;
import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.dto.request.court.CourtRefreshRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.dto.response.court.CourtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourtAdapter {
    private final CourtProperties courtProperties;
    private final RestClient courtRestClient;

    public CourtTokenResponse sendLoginRequest() {

        CourtAuthRequest request = new CourtAuthRequest(courtProperties.getLogin(), courtProperties.getPassword());

        var response = courtRestClient.post().uri("/auth/login").body(request).retrieve().toEntity(CourtTokenResponse.class);

        log.info("Login response: {}", response);

        return response.getBody();
    }

    public CourtTokenResponse sendRefreshRequest(CourtRefreshRequest request) {

        var response = courtRestClient.post().uri("/auth/refresh").body(request).retrieve().toEntity(CourtTokenResponse.class);

        log.info("Refresh response: {}", response);

        return response.getBody();
    }

    public CourtOffenseResponse sendOffenseRegistrationRequest(String authToken, CourtOffenseRegistrationRequest request) {

        var response = courtRestClient.post().uri("/legal-offenses/registration").body(request).headers(
            httpHeaders -> httpHeaders.setBearerAuth(authToken)
        ).retrieve().toEntity(CourtOffenseResponse.class);

        log.info("Send offense response: {}", response);

        return response.getBody();
    }
}
