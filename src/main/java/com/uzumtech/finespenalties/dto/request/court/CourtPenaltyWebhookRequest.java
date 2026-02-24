package com.uzumtech.finespenalties.dto.request.court;

import com.uzumtech.finespenalties.constant.enums.PenaltyType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CourtPenaltyWebhookRequest(Long id,
                                         Long externalOffenseId,
                                         PenaltyType type,
                                         Long bhmAmountAtTime,
                                         BigDecimal bhmMultiplier,
                                         OffsetDateTime dueDate,
                                         String courtDecisionText,
                                         String qualification,
                                         Integer deprivationDurationMonths,
                                         OffsetDateTime createdAt) {}
