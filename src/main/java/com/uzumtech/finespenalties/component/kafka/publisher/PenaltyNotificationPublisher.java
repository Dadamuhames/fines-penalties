package com.uzumtech.finespenalties.component.kafka.publisher;

import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.constant.NotificationTemplates;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.dto.event.PenaltyNotificationEvent;
import com.uzumtech.finespenalties.service.intr.notification.NotificationRequestStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PenaltyNotificationPublisher implements EventPublisher<PenaltyNotificationEvent> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NotificationRequestStoreService requestStoreService;


    @Override
    public void publish(PenaltyNotificationEvent event) {
        UUID requestId = UUID.randomUUID();

        String notificationText = String.format(NotificationTemplates.PENALTY_NOTIFICATION, event.penaltyId());

        var notificationEvent = new NotificationEvent(requestId, notificationText, event.userEmail(), NotificationType.EMAIL);

        requestStoreService.createNotificationRequest(notificationEvent);

        kafkaTemplate.send(KafkaConstants.NOTIFICATION_TOPIC, notificationEvent);
    }
}
