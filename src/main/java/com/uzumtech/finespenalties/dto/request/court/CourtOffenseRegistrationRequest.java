package com.uzumtech.finespenalties.dto.request.court;

import java.time.OffsetDateTime;

public record CourtOffenseRegistrationRequest(Long legalOffenseId,
                                              String offenderPinfl,
                                              String offenseLocation,
                                              String protocolNumber,
                                              String offenderExplanation,
                                              String description,
                                              OffsetDateTime offenseDateTime,
                                              String codeArticleReference) {}
