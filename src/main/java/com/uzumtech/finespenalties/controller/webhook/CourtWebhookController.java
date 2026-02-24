package com.uzumtech.finespenalties.controller.webhook;

import com.uzumtech.finespenalties.configuration.property.service.CourtProperties;
import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.court.CourtPenaltyWebhookRequest;
import com.uzumtech.finespenalties.exception.WebhookAccessDeniedException;
import com.uzumtech.finespenalties.service.intr.court.CourtPenaltyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fines-penalties/court")
public class CourtWebhookController {

    private final CourtProperties courtProperties;
    private final CourtPenaltyService courtPenaltyService;

    @PostMapping("/penalty-webhook")
    public ResponseEntity<Void> registerPenalty(@RequestHeader("X-Court-Secret") String courtSecret, @Valid @RequestBody CourtPenaltyWebhookRequest request) {

        if (courtSecret == null || !courtSecret.equals(courtProperties.getWebhookSecret())) {
            throw new WebhookAccessDeniedException(ErrorCode.COURT_WEBHOOK_SECRET_INVALID_CODE);
        }

        courtPenaltyService.registerPenaltyFromWebhook(request);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
