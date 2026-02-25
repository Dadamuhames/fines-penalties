package com.uzumtech.finespenalties.service.intr.court;

public interface CourtAuthService {

    void refreshTokens();

    String getAuthToken();
}
