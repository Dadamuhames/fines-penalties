package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.PenaltyStatus;
import com.uzumtech.finespenalties.constant.enums.PenaltyType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PenaltyDetailResponse(Long id,
                                    PenaltyType type,
                                    PenaltyStatus status,
                                    Long bhmAmountAtTime,
                                    BigDecimal bhmMultiplier,
                                    OffsetDateTime dueDate,
                                    String courtDecisionText,
                                    String qualification,
                                    Integer deprivationDurationMonths,
                                    OffsetDateTime createdAt) {
}
