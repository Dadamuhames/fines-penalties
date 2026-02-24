package com.uzumtech.finespenalties.service.impl.court;

import com.uzumtech.finespenalties.component.adapter.CourtAdapter;
import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.dto.response.court.CourtOffenseResponse;
import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import com.uzumtech.finespenalties.service.intr.court.CourtHelperService;
import com.uzumtech.finespenalties.service.intr.court.CourtOffenseRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtOffenseRegistrationServiceImpl implements CourtOffenseRegistrationService {

    private final CourtAuthService courtAuthService;
    private final CourtHelperService courtHelperService;
    private final CourtAdapter courtAdapter;


    public CourtOffenseResponse sendOffenseToCourt(Long offenseId) {
        CourtOffenseRegistrationRequest request = courtHelperService.getRegistrationRequest(offenseId);

        String accessToken = courtAuthService.getAuthToken();

        return courtAdapter.sendOffenseRegistrationRequest(accessToken, request);
    }
}
