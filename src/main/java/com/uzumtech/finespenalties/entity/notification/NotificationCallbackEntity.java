package com.uzumtech.finespenalties.entity.notification;

import com.uzumtech.finespenalties.constant.enums.NotificationStatus;
import com.uzumtech.finespenalties.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_callbacks")
public class NotificationCallbackEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_request_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private NotificationRequestEntity notificationRequest;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "status")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NotificationStatus status;
}
