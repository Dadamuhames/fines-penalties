package com.uzumtech.finespenalties.entity.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationRequestStatus;
import com.uzumtech.finespenalties.constant.enums.NotificationStatus;
import com.uzumtech.finespenalties.constant.enums.NotificationType;
import com.uzumtech.finespenalties.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_requests", indexes = @Index(columnList = "requestId"))
public class NotificationRequestEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID requestId;

    @Column(unique = true)
    private Long notificationServiceId;

    @Column(nullable = false)
    private String notificationReceiver;

    @Column(nullable = false)
    private String notificationText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "notification_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "request_status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NotificationRequestStatus requestStatus;
}
