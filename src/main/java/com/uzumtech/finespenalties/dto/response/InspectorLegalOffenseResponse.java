package com.uzumtech.finespenalties.dto.response;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;

import java.time.OffsetDateTime;

public record InspectorLegalOffenseResponse(Long id, String protocolNumber, OffenseStatus status,
                                            String offenderFullName, String offenseLocation,
                                            OffsetDateTime createdAt) {}
