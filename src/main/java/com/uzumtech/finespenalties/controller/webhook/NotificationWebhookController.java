package com.uzumtech.finespenalties.controller.webhook;

import com.uzumtech.finespenalties.dto.request.NotificationCallbackRequest;
import com.uzumtech.finespenalties.service.intr.notification.NotificationCallbackStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/notification-service")
public class NotificationWebhookController {

    private final NotificationCallbackStoreService callbackStoreService;

    @PostMapping("/callback")
    public ResponseEntity<Void> receiveCallback(@Valid @RequestBody NotificationCallbackRequest request) {

        callbackStoreService.receiveNotificationWebhook(request);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
