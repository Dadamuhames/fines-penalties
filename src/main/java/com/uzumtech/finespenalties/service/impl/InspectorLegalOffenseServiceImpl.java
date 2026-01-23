package com.uzumtech.finespenalties.service.impl;

import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.mapper.LegalOffenseMapper;
import com.uzumtech.finespenalties.repository.LegalOffenceRepository;
import com.uzumtech.finespenalties.service.intr.InspectorLegalOffenseService;
import com.uzumtech.finespenalties.utils.ProtocolNumberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class InspectorLegalOffenseServiceImpl implements InspectorLegalOffenseService {
    private final LegalOffenceRepository legalOffenceRepository;
    private final ProtocolNumberUtils protocolNumberUtils;
    private final LegalOffenseMapper legalOffenseMapper;

    @Override
    public Page<InspectorLegalOffenseResponse> findAllForInspector(InspectorEntity inspector, Pageable pageable) {
        Page<LegalOffenseEntity> legalOffenses = legalOffenceRepository.findByInspectorId(inspector.getId(), pageable);

        return legalOffenses.map(legalOffenseMapper::entityToInspectorResponse);
    }

    @Override
    public InspectorLegalOffenseResponse registerLegalOffense(InspectorEntity inspector, LegalOffenceRegisterRequest request) {
        LegalOffenseEntity legalOffense = legalOffenseMapper.requestToEntity(request, inspector);

        // store in DB
        String protocolNumber = protocolNumberUtils.generateProtocolNumber();
        legalOffense.setProtocolNumber(protocolNumber);

        legalOffense = legalOffenceRepository.save(legalOffense);

        // send to J-Court through Kafka
        // TODO: send offense with Kafka

        return legalOffenseMapper.entityToInspectorResponse(legalOffense);
    }
}
