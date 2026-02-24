package com.uzumtech.finespenalties.service.impl.user;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.response.LegalOffenseDetailResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.exception.OffenseNotFoundException;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import com.uzumtech.finespenalties.service.intr.user.UserLegalOffenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserLegalOffenseServiceImpl implements UserLegalOffenseService {

    private final LegalOffenseRepository legalOffenseRepository;
    private final LegalOffenseMapper legalOffenseMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LegalOffenseResponse> list(UserEntity user, Pageable pageable) {
        Page<LegalOffenseEntity> legalOffenses = legalOffenseRepository.findByUserId(user.getId(), pageable);

        return legalOffenses.map(legalOffenseMapper::entityToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LegalOffenseDetailResponse getOne(Long id, UserEntity user) {

        LegalOffenseEntity legalOffense = legalOffenseRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
            () -> new OffenseNotFoundException(ErrorCode.OFFENSE_NOT_FOUND_CODE, HttpStatus.NOT_FOUND)
        );

        return legalOffenseMapper.entityToDetailResponse(legalOffense);
    }
}
