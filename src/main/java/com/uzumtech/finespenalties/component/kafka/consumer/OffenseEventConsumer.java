package com.uzumtech.finespenalties.component.kafka.consumer;

import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.event.OffenseEvent;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.exception.http.HttpClientException;
import com.uzumtech.finespenalties.exception.http.HttpServerException;
import com.uzumtech.finespenalties.exception.kafka.nontransients.HttpRequestInvalidException;
import com.uzumtech.finespenalties.exception.kafka.transients.CourtAuthFailedException;
import com.uzumtech.finespenalties.exception.kafka.transients.HttpServerUnavailableException;
import com.uzumtech.finespenalties.exception.kafka.transients.TransientException;
import com.uzumtech.finespenalties.service.intr.OffenseService;
import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import com.uzumtech.finespenalties.service.intr.court.CourtOffenseRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OffenseEventConsumer implements EventConsumer<OffenseEvent> {

    private final CourtOffenseRegistrationService offenseRegistrationService;
    private final CourtAuthService courtAuthService;
    private final OffenseService offenseService;


    @RetryableTopic(attempts = "6", backOff = @BackOff(delay = 5000), include = {TransientException.class}, numPartitions = "3", replicationFactor = "1")
    @KafkaListener(topics = KafkaConstants.OFFENSE_TOPIC, groupId = KafkaConstants.OFFENSE_GROUP_ID)
    public void listen(@Payload @Valid OffenseEvent event) {

        try {
            CourtOffenseResponse offenseResponse = offenseRegistrationService.sendOffenseToCourt(event.offenseId());

            offenseService.updateWithCourtData(offenseResponse);

        } catch (HttpServerException ex) {

            throw new HttpServerUnavailableException(ErrorCode.COURT_SERVICE_UNAVAILABLE_CODE, ex);

        } catch (HttpClientException ex) {

            // if HTTP Status == 401 => Court auth failed => flushing tokens so they are re-fetched on retry
            // and throwing TransientException
            if (ex.getStatus() == HttpStatus.UNAUTHORIZED || ex.getStatus() == HttpStatus.FORBIDDEN) {
                courtAuthService.flushTokens();

                throw new CourtAuthFailedException(ErrorCode.COURT_AUTH_FAILED_CODE, ex);
            }

            throw new HttpRequestInvalidException(ErrorCode.COURT_REQUEST_INVALID_CODE, ex);
        }

    }


    @DltHandler
    public void dltHandler(OffenseEvent event, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {

        log.info("OffenseEvent with ID: {}, sent to DLT with exception: {}", event.offenseId(), exceptionMessage);

        offenseService.changeStatus(event.offenseId(), OffenseStatus.FAILED_TO_SEND);
    }
}
