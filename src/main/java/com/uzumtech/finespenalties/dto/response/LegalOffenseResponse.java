package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.OffenceStatus;

import java.time.OffsetDateTime;

public record LegalOffenseResponse(Long id, String protocolNumber, OffenceStatus status, InspectorDto inspector,
                                   CodeArticleDto codeArticle, String offenceLocation, OffsetDateTime createdAt) {}
