package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;

import java.time.OffsetDateTime;

public record LegalOffenseDetailResponse(Long id,
                                         String protocolNumber,
                                         InspectorDto inspector,
                                         CodeArticleDto codeArticle,
                                         String description,
                                         String offenseLocation,
                                         String offenderExplanation,
                                         String courtCaseNumber,
                                         OffenseStatus status,
                                         OffsetDateTime offenseDateTime,
                                         OffsetDateTime createdAt,
                                         PenaltyDetailResponse penalty) {
}
