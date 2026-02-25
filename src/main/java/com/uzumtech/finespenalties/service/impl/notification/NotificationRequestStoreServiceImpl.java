package com.uzumtech.finespenalties.service.impl.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import com.uzumtech.finespenalties.repository.notification.NotificationRequestRepository;
import com.uzumtech.finespenalties.service.intr.notification.NotificationRequestStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationRequestStoreServiceImpl implements NotificationRequestStoreService {
    private final NotificationRequestRepository notificationRequestRepository;

    @Transactional
    public boolean isAvailableForProcessing(UUID requestId) {
        return notificationRequestRepository.isAvailableForProcessing(requestId);
    }

    @Transactional
    public void createNotificationRequest(NotificationEvent event) {
        NotificationRequestEntity entity = NotificationRequestEntity.builder().notificationType(event.type()).notificationText(event.text()).requestId(event.requestId()).requestStatus(NotificationRequestStatus.NEW).notificationReceiver(event.receiver()).build();

        notificationRequestRepository.save(entity);
    }

    @Transactional
    public void markAsDelivered(UUID requestId, Long notificationServiceId) {
        notificationRequestRepository.markAsDelivered(requestId, notificationServiceId);
    }

    @Transactional
    public void changeStatus(UUID requestId, NotificationRequestStatus status) {
        notificationRequestRepository.updateRequestStatus(requestId, status);
    }
}
