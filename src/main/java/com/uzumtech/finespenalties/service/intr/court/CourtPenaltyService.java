package com.uzumtech.finespenalties.service.intr.court;

import com.uzumtech.finespenalties.dto.request.court.CourtPenaltyWebhookRequest;

public interface CourtPenaltyService {

    void registerPenaltyFromWebhook(CourtPenaltyWebhookRequest request);
}
