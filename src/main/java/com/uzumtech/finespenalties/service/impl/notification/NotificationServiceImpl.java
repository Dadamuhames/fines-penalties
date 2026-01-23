package com.uzumtech.finespenalties.service.impl.notification;


import com.uzumtech.finespenalties.dto.request.NotificationRequest;
import com.uzumtech.finespenalties.dto.response.NotificationResponse;
import com.uzumtech.finespenalties.service.intr.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final RestClient notificationRestClient;

    public NotificationResponse sendOtpNotification(final NotificationRequest request) {
        var response = notificationRestClient.post().uri("/sending").body(request).retrieve().toEntity(NotificationResponse.class);

        return response.getBody();
    }
}
