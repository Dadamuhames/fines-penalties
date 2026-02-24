package com.uzumtech.finespenalties.component.adapter;


import com.uzumtech.finespenalties.dto.response.GcpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GcpAdapter {
    private final RestClient gcpRestClient;

    public GcpResponse fetchUserInfoByPinfl(final String pinfl) {
        String url = String.format("/users/by-pi/%s", pinfl);

        var response = gcpRestClient.get().uri(url).retrieve().toEntity(GcpResponse.class);

        return response.getBody();
    }
}
