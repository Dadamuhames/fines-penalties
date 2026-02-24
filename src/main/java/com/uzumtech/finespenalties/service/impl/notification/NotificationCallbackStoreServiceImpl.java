package com.uzumtech.finespenalties.service.impl.notification;

import com.uzumtech.finespenalties.constant.enums.ErrorCode;
import com.uzumtech.finespenalties.dto.request.NotificationCallbackRequest;
import com.uzumtech.finespenalties.entity.notification.NotificationCallbackEntity;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import com.uzumtech.finespenalties.exception.NotificationIdInvalidException;
import com.uzumtech.finespenalties.mapper.NotificationMapper;
import com.uzumtech.finespenalties.repository.notification.NotificationCallbackRepository;
import com.uzumtech.finespenalties.repository.notification.NotificationRequestRepository;
import com.uzumtech.finespenalties.service.intr.notification.NotificationCallbackStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationCallbackStoreServiceImpl implements NotificationCallbackStoreService {
    private final NotificationCallbackRepository callbackRepository;
    private final NotificationRequestRepository notificationRequestRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void receiveNotificationWebhook(NotificationCallbackRequest callbackRequest) {

        NotificationRequestEntity notificationRequest = getRequestByNotificationId(callbackRequest.content().notificationId());

        NotificationCallbackEntity callbackEntity = notificationMapper.requestToEntity(callbackRequest, notificationRequest);

        callbackRepository.save(callbackEntity);
    }


    private NotificationRequestEntity getRequestByNotificationId(Long notificationId) {
        return notificationRequestRepository.findByNotificationServiceId(notificationId).orElseThrow(() -> new NotificationIdInvalidException(ErrorCode.NOTIFICATION_ID_INVALID_CODE));
    }

}
