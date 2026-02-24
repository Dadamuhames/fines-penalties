package com.uzumtech.finespenalties.mapper;

import com.uzumtech.finespenalties.dto.request.court.CourtPenaltyWebhookRequest;
import com.uzumtech.finespenalties.dto.response.PenaltyDetailResponse;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.PenaltyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PenaltyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courtPenaltyId", source = "request.id")
    @Mapping(target = "offense", source = "offense")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "courtDecisionDate", source = "request.createdAt")
    PenaltyEntity webhookRequestToEntity(CourtPenaltyWebhookRequest request, LegalOffenseEntity offense);

    PenaltyDetailResponse entityToDetailResponse(PenaltyEntity penalty);
}
