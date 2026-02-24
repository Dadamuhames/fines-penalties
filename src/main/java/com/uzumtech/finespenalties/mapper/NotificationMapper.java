package com.uzumtech.finespenalties.mapper;

import com.uzumtech.finespenalties.dto.request.NotificationCallbackRequest;
import com.uzumtech.finespenalties.entity.notification.NotificationCallbackEntity;
import com.uzumtech.finespenalties.entity.notification.NotificationRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "notificationRequest", source = "notificationRequest")
    @Mapping(target = "status", source = "request.content.status")
    NotificationCallbackEntity requestToEntity(NotificationCallbackRequest request, NotificationRequestEntity notificationRequest);

}
