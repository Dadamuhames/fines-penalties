package com.uzumtech.finespenalties.component.adapter;


import com.uzumtech.finespenalties.dto.request.NotificationRequest;
import com.uzumtech.finespenalties.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NotificationAdapter {
    private final RestClient notificationRestClient;

    public NotificationResponse sendNotification(NotificationRequest request) {
        var response = notificationRestClient.post().uri("/sending").body(request).retrieve().toEntity(NotificationResponse.class);

        return response.getBody();
    }
}
