package com.uzumtech.finespenalties.service.impl.kafka.publisher;

import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.constant.NotificationTemplates;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import com.uzumtech.finespenalties.service.intr.KafkaEventPublisherService;
import com.uzumtech.finespenalties.service.impl.notification.NotificationRequestStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpPublisherService implements KafkaEventPublisherService<OtpSendEvent> {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final NotificationRequestStoreService requestStoreService;

    @Override
    @Transactional
    public void publish(OtpSendEvent event) {
        UUID requestId = UUID.randomUUID();

        String notificationText = String.format(NotificationTemplates.OTP_NOTIFICATION, event.code());

        var notificationEvent = new NotificationEvent(requestId, notificationText, event.phone());

        requestStoreService.createNotificationRequest(notificationEvent, NotificationType.SMS);

        kafkaTemplate.send(KafkaConstants.OTP_TOPIC, notificationEvent);
    }
}
