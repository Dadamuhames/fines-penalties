package com.uzumtech.finespenalties.service.intr.court;

import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.PenaltyEntity;

public interface CourtHelperService {

    CourtOffenseRegistrationRequest getRegistrationRequest(Long offenseId);

    LegalOffenseEntity findOffenseByIdAndSentToCourt(Long offenseId);

    PenaltyEntity savePenaltyAndChangeOffenseStatus(Long offenseId, PenaltyEntity penalty);
}
