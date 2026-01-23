package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InspectorLegalOffenseService {

    Page<InspectorLegalOffenseResponse> findAllForInspector(InspectorEntity inspector, Pageable pageable);

    InspectorLegalOffenseResponse registerLegalOffense(InspectorEntity inspector, LegalOffenceRegisterRequest request);
}
