package com.uzumtech.finespenalties.repository.notification;

import com.uzumtech.finespenalties.entity.notification.NotificationCallbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationCallbackRepository extends JpaRepository<NotificationCallbackEntity, Long> {}
