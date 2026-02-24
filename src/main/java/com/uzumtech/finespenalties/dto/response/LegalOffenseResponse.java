package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;

import java.time.OffsetDateTime;

public record LegalOffenseResponse(Long id, String protocolNumber, OffenseStatus status, InspectorDto inspector,
                                   CodeArticleDto codeArticle, String offenceLocation, OffsetDateTime createdAt) {}
