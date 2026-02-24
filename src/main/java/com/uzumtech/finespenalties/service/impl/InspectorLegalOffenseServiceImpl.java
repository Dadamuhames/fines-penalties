package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.component.kafka.publisher.OffenseEventPublisher;
import com.uzumtech.finespenalties.dto.event.OffenseEvent;
import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenseRepository;
import com.uzumtech.finespenalties.service.intr.CodeArticleService;
import com.uzumtech.finespenalties.service.intr.InspectorLegalOffenseService;
import com.uzumtech.finespenalties.service.intr.OffenseService;
import com.uzumtech.finespenalties.service.intr.user.UserRegisterService;
import com.uzumtech.finespenalties.utils.ProtocolNumberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class InspectorLegalOffenseServiceImpl implements InspectorLegalOffenseService {
    private final LegalOffenseRepository legalOffenseRepository;
    private final UserRegisterService userRegisterService;
    private final ProtocolNumberUtils protocolNumberUtils;
    private final LegalOffenseMapper legalOffenseMapper;
    private final OffenseEventPublisher offenseEventPublisher;
    private final CodeArticleService codeArticleService;
    private final OffenseService offenseService;

    @Override
    @Transactional(readOnly = true)
    public Page<InspectorLegalOffenseResponse> findAllForInspector(InspectorEntity inspector, Pageable pageable) {
        Page<LegalOffenseEntity> legalOffenses = legalOffenseRepository.findByInspectorId(inspector.getId(), pageable);

        return legalOffenses.map(legalOffenseMapper::entityToInspectorResponse);
    }

    @Override
    public InspectorLegalOffenseResponse registerLegalOffense(InspectorEntity inspector, LegalOffenceRegisterRequest request) {
        UserEntity user = userRegisterService.findUserByPinflOrRegister(request.offenderPinfl());

        CodeArticleEntity codeArticle = codeArticleService.findByIdOrThrowBadRequestException(request.codeArticleId());

        LegalOffenseEntity legalOffense = legalOffenseMapper.requestToEntity(request, inspector, user, codeArticle);

        // store in DB
        String protocolNumber = protocolNumberUtils.generateProtocolNumber();
        legalOffense.setProtocolNumber(protocolNumber);

        LegalOffenseEntity savedOffense = offenseService.saveOffense(legalOffense);

        // send to J-Court through Kafka
        OffenseEvent offenseEvent = new OffenseEvent(savedOffense.getId());
        offenseEventPublisher.publish(offenseEvent);

        return legalOffenseMapper.entityToInspectorResponse(savedOffense, user.getFullName());
    }
}
