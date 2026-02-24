package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;

public interface OffenseService {

    void updateWithCourtData(CourtOffenseResponse offenseResponse);

    void changeStatus(Long offenseId, OffenseStatus status);

    LegalOffenseEntity saveOffense(LegalOffenseEntity legalOffense);
}
