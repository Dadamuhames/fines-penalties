package com.uzumtech.finespenalties.service.intr.court;

public interface CourtAuthService {

    String getAuthToken();

    void flushTokens();
}
