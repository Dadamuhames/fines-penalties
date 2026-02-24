package com.uzumtech.finespenalties.service.intr.court;

import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;

public interface CourtOffenseRegistrationService {

    CourtOffenseResponse sendOffenseToCourt(Long offenseId);
}
