package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.request.court.CourtOffenseRegistrationRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseDetailResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
import com.uzumtech.finespenalties.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LegalOffenseMapper {

    @Autowired
    public InspectorMapper inspectorMapper;
    @Autowired
    public CodeArticleMapper codeArticleMapper;

    @Autowired
    public PenaltyMapper penaltyMapper;


    @Mapping(target = "inspector", expression = "java(inspectorMapper.inspectorToDto(entity.getInspector()))")
    @Mapping(target = "codeArticle", expression = "java(codeArticleMapper.entityToDto(entity.getCodeArticle()))")
    public abstract LegalOffenseResponse entityToResponse(LegalOffenseEntity entity);

    @Mapping(target = "offenderFullName", source = "entity.user.fullName")
    public abstract InspectorLegalOffenseResponse entityToInspectorResponse(LegalOffenseEntity entity);

    @Mapping(target = "offenderFullName", source = "offenderFullName")
    public abstract InspectorLegalOffenseResponse entityToInspectorResponse(LegalOffenseEntity entity, String offenderFullName);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "inspector", source = "inspector")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "codeArticle", source = "codeArticle")
    @Mapping(target = "protocolNumber", source = "protocolNumber")
    public abstract LegalOffenseEntity requestToEntity(LegalOffenceRegisterRequest request, InspectorEntity inspector, UserEntity user, CodeArticleEntity codeArticle, String protocolNumber);


    @Mapping(target = "legalOffenseId", source = "offenseEntity.id")
    @Mapping(target = "offenderPinfl", source = "offenseEntity.user.pinfl")
    @Mapping(target = "codeArticleReference", source = "offenseEntity.codeArticle.reference")
    public abstract CourtOffenseRegistrationRequest entityToRegistrationRequest(LegalOffenseEntity offenseEntity);

    @Mapping(target = "inspector", expression = "java(inspectorMapper.inspectorToDto(entity.getInspector()))")
    @Mapping(target = "codeArticle", expression = "java(codeArticleMapper.entityToDto(entity.getCodeArticle()))")
    @Mapping(target = "penalty", expression = "java(penaltyMapper.entityToDetailResponse(entity.getPenalty()))")
    public abstract LegalOffenseDetailResponse entityToDetailResponse(LegalOffenseEntity entity);
}
