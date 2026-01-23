package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenceRepository;
import com.uzumtech.finespenalties.service.intr.UserLegalOffenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserLegalOffenseServiceImpl implements UserLegalOffenseService {
    private final LegalOffenceRepository legalOffenceRepository;
    private final LegalOffenseMapper legalOffenseMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LegalOffenseResponse> list(UserEntity user, Pageable pageable) {
        Page<LegalOffenseEntity> legalOffenses = legalOffenceRepository.findByOffenderPinfl(user.getPinfl(), pageable);

        return legalOffenses.map(legalOffenseMapper::entityToResponse);
    }


}
