package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import com.uzumtech.finespenalties.service.intr.OffenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OffenseServiceImpl implements OffenseService {

    private final LegalOffenseRepository legalOffenseRepository;

    @Transactional
    public void updateWithCourtData(CourtOffenseResponse offenseResponse) {
        legalOffenseRepository.updateWithCourtData(offenseResponse.externalId(), offenseResponse.id(), offenseResponse.courtCaseNumber());
    }

    @Transactional
    public void changeStatus(Long offenseId, OffenseStatus status) {
        legalOffenseRepository.updateStatus(offenseId, status);
    }

    @Transactional
    public LegalOffenseEntity saveOffense(LegalOffenseEntity legalOffense) {
        return legalOffenseRepository.save(legalOffense);
    }
}
