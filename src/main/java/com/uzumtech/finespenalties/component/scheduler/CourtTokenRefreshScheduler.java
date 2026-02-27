package com.uzumtech.finespenalties.component.scheduler;

import com.uzumtech.finespenalties.service.intr.court.CourtAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourtTokenRefreshScheduler {

    private final CourtAuthService courtAuthService;

    @Scheduled(
        fixedRateString = "${scheduler.token-refresh-rate}",
        initialDelayString = "${scheduler.token-refresh-init-delay}"
    )
    public void refreshTokens() {

        courtAuthService.refreshTokens();

    }

}
