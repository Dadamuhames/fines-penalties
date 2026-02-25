package com.uzumtech.finespenalties.service.intr;

import com.uzumtech.finespenalties.dto.request.InspectorLoginRequest;
import com.uzumtech.finespenalties.dto.response.TokenResponse;

public interface InspectorAuthService {
    TokenResponse login(InspectorLoginRequest request);
}
