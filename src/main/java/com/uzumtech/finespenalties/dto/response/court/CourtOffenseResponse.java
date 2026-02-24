package com.uzumtech.finespenalties.dto.response.court;

import java.time.OffsetDateTime;

public record CourtOffenseResponse(Long id, Long externalId, String courtCaseNumber, OffsetDateTime createdAt) {}

