package com.uzumtech.finespenalties.component.kafka.consumer;

import com.uzumtech.finespenalties.component.adapter.NotificationAdapter;
import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.dto.request.NotificationRequest;
import com.uzumtech.finespenalties.dto.response.NotificationResponse;
import com.uzumtech.finespenalties.exception.http.HttpClientException;
import com.uzumtech.finespenalties.exception.http.HttpServerException;
import com.uzumtech.finespenalties.exception.kafka.nontransients.HttpRequestInvalidException;
import com.uzumtech.finespenalties.exception.kafka.transients.HttpServerUnavailableException;
import com.uzumtech.finespenalties.exception.kafka.transients.TransientException;
import com.uzumtech.finespenalties.service.intr.notification.NotificationRequestStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer implements EventConsumer<NotificationEvent> {
    private final NotificationAdapter notificationAdapter;
    private final NotificationRequestStoreService requestStoreService;

    @RetryableTopic(attempts = "6", backOff = @BackOff(delay = 5000), include = {TransientException.class}, numPartitions = "3", replicationFactor = "1")
    @KafkaListener(topics = KafkaConstants.NOTIFICATION_TOPIC, groupId = KafkaConstants.NOTIFICATION_GROUP_ID)
    public void listen(@Payload @Valid NotificationEvent event) {

        UUID requestId = event.requestId();

        boolean isAvailable = requestStoreService.isAvailableForProcessing(requestId);

        if (!isAvailable) {
            log.info("Notification with requestId: {} is already being processed or in the terminal status", requestId);
            return;
        }

        log.info("Notification processing: {}", event);

        NotificationRequest request = NotificationRequest.of(event);

        try {
            NotificationResponse response = notificationAdapter.sendNotification(request);

            requestStoreService.markAsDelivered(requestId, response.data().notificationId());

        } catch (HttpServerException ex) {
            requestStoreService.changeStatus(requestId, NotificationRequestStatus.SENT_TO_RETRY);

            log.error("Notification Http Server Error: {}", ex.getMessage());

            throw new HttpServerUnavailableException(ex);

        } catch (HttpClientException ex) {

            log.error("Notification Http Client Error: {}", ex.getMessage());

            throw new HttpRequestInvalidException(ErrorCode.NOTIFICATION_REQUEST_INVALID_CODE, ex);
        }
    }

    @DltHandler
    public void dltHandler(NotificationEvent event, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("Event failed: {}, with exception: {}", event, exceptionMessage);
        requestStoreService.changeStatus(event.requestId(), NotificationRequestStatus.FAILED);
    }
}
