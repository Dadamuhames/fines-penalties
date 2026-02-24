package com.uzumtech.finespenalties.service.impl.court;


import com.uzumtech.finespenalties.component.kafka.publisher.PenaltyNotificationPublisher;
import com.uzumtech.finespenalties.dto.event.PenaltyNotificationEvent;
import com.uzumtech.finespenalties.dto.request.court.CourtPenaltyWebhookRequest;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.PenaltyEntity;
import com.uzumtech.finespenalties.mapper.PenaltyMapper;
import com.uzumtech.finespenalties.service.intr.court.CourtHelperService;
import com.uzumtech.finespenalties.service.intr.court.CourtPenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourtPenaltyServiceImpl implements CourtPenaltyService {

    private final CourtHelperService courtHelperService;
    private final PenaltyNotificationPublisher notificationPublisher;
    private final PenaltyMapper penaltyMapper;


    public void registerPenaltyFromWebhook(CourtPenaltyWebhookRequest request) {

        LegalOffenseEntity offense = courtHelperService.findOffenseByIdAndSentToCourt(request.externalOffenseId());

        PenaltyEntity penalty = penaltyMapper.webhookRequestToEntity(request, offense);

        PenaltyEntity savedPenalty = courtHelperService.savePenaltyAndChangeOffenseStatus(offense.getId(), penalty);

        notificationPublisher.publish(new PenaltyNotificationEvent(savedPenalty.getId(), offense.getUser().getEmail()));
    }
}
