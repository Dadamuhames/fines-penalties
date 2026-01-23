package com.uzumtech.finespenalties.service.impl.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import com.uzumtech.finespenalties.repository.notification.NotificationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationRequestStoreService {
    private final NotificationRequestRepository notificationRequestRepository;

    public int claimForProcessing(final UUID requestId) {
        return notificationRequestRepository.claimForProcessing(requestId);
    }

    @Transactional
    public void createNotificationRequest(final NotificationEvent event, final NotificationType type) {
        NotificationRequestEntity entity = NotificationRequestEntity.builder().notificationType(type).notificationText(event.text()).requestId(event.requestId()).requestStatus(NotificationRequestStatus.NEW).notificationReceiver(event.receiver()).build();

        notificationRequestRepository.save(entity);
    }


    @Transactional
    public void markAsDelivered(final UUID requestId, final Long notificationServiceId) {
        notificationRequestRepository.markAsDelivered(requestId, notificationServiceId);
    }

    @Transactional
    public void changeStatus(final UUID requestId, final NotificationRequestStatus status) {
        notificationRequestRepository.updateRequestStatus(requestId, status);
    }

}
