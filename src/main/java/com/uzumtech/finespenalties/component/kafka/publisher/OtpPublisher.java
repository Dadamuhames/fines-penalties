package com.uzumtech.finespenalties.component.kafka.publisher;

import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.constant.NotificationTemplates;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import com.uzumtech.finespenalties.service.intr.notification.NotificationRequestStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OtpPublisher implements EventPublisher<OtpSendEvent> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NotificationRequestStoreService requestStoreService;

    @Override
    public void publish(OtpSendEvent event) {
        UUID requestId = UUID.randomUUID();

        String notificationText = String.format(NotificationTemplates.OTP_NOTIFICATION, event.code());

        var notificationEvent = new NotificationEvent(requestId, notificationText, event.phone(), NotificationType.SMS);

        requestStoreService.createNotificationRequest(notificationEvent);

        kafkaTemplate.send(KafkaConstants.NOTIFICATION_TOPIC, notificationEvent);
    }
}
