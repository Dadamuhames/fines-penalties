package com.uzumtech.finespenalties.service.impl.kafka.consumer;

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
import com.uzumtech.finespenalties.service.intr.ConsumerService;
import com.uzumtech.finespenalties.service.impl.notification.NotificationRequestStoreService;

import com.uzumtech.finespenalties.service.intr.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpConsumerService implements ConsumerService<NotificationEvent> {
    private final NotificationService notificationService;
    private final NotificationRequestStoreService requestStoreService;

    @RetryableTopic(attempts = "6", backoff = @Backoff(delay = 5000), include = {TransientException.class}, numPartitions = "3", replicationFactor = "1")
    @KafkaListener(topics = KafkaConstants.OTP_TOPIC, groupId = KafkaConstants.OTP_GROUP_ID)
    public void listen(@Payload @Valid NotificationEvent event) {
        int claim = requestStoreService.claimForProcessing(event.requestId());

        if (claim == 0) {
            log.info("Notification with requestId: {} is already being processed or in the terminal status", event.requestId());
            return;
        }

        NotificationRequest request = NotificationRequest.sms(event.receiver(), event.text());

        try {
            NotificationResponse response = notificationService.sendOtpNotification(request);

            requestStoreService.markAsDelivered(event.requestId(), response.data().notificationId());

        } catch (HttpServerException ex) {
            requestStoreService.changeStatus(event.requestId(), NotificationRequestStatus.SENT_TO_RETRY);
            throw new HttpServerUnavailableException(ex);

        } catch (HttpClientException ex) {
            throw new HttpRequestInvalidException(ErrorCode.NOTIFICATION_REQUEST_INVALID_CODE, ex);
        }
    }

    @DltHandler
    public void dltHandler(NotificationEvent event, String exceptionMessage) {
        log.error("Event failed: {}, with exception: {}", event, exceptionMessage);
        requestStoreService.changeStatus(event.requestId(), NotificationRequestStatus.FAILED);
    }
}
