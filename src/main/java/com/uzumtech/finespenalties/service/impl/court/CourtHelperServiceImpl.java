package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.constant.enums.OffenseStatus;
import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.PenaltyEntity;
import com.uzumtech.finespenalties.exception.OffenseNotFoundException;
import com.uzumtech.finespenalties.exception.kafka.nontransients.OffenseIdInvalidException;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import com.uzumtech.finespenalties.repository.PenaltyRepository;
import com.uzumtech.finespenalties.service.intr.court.CourtHelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourtHelperServiceImpl implements CourtHelperService {

    private final LegalOffenseRepository offenseRepository;
    private final PenaltyRepository penaltyRepository;
    private final LegalOffenseMapper offenseMapper;


    @Transactional(readOnly = true)
    public CourtOffenseRegistrationRequest getRegistrationRequest(Long offenseId) {
        LegalOffenseEntity offenseEntity = offenseRepository.findByIdAndStatus(offenseId, OffenseStatus.NEW).orElseThrow(() -> new OffenseIdInvalidException(ErrorCode.OFFENSE_ID_INVALID_CODE));

        return offenseMapper.entityToRegistrationRequest(offenseEntity);
    }

    @Transactional(readOnly = true)
    public LegalOffenseEntity findOffenseByIdAndSentToCourt(Long offenseId) {
        return offenseRepository.findByIdAndStatus(offenseId, OffenseStatus.SENT_TO_COURT).orElseThrow(
            () -> new OffenseNotFoundException(ErrorCode.OFFENSE_NOT_FOUND_CODE)
        );
    }

    @Transactional
    public PenaltyEntity savePenaltyAndChangeOffenseStatus(Long offenseId, PenaltyEntity penalty) {

        offenseRepository.updateStatus(offenseId, OffenseStatus.PENALTY_RULED_OUT);

        return penaltyRepository.save(penalty);
    }

}
