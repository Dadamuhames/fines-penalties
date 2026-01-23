package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.OffenceStatus;

import java.time.OffsetDateTime;

public record InspectorLegalOffenseResponse(Long id, String protocolNumber, OffenceStatus status,
                                            String offenderFullName, String offenseLocation,
                                            OffsetDateTime createdAt) {}
