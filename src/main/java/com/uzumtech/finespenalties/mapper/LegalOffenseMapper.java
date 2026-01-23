package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.request.LegalOffenceRegisterRequest;
import com.uzumtech.finespenalties.dto.response.InspectorLegalOffenseResponse;
import com.uzumtech.finespenalties.dto.response.LegalOffenseResponse;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import com.uzumtech.finespenalties.entity.LegalOffenseEntity;
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

    @Mapping(target = "inspector", expression = "java(inspectorMapper.inspectorToDto(entity.getInspector()))")
    @Mapping(target = "codeArticle", expression = "java(codeArticleMapper.entityToDto(entity.getCodeArticle()))")
    public abstract LegalOffenseResponse entityToResponse(LegalOffenseEntity entity);

    public abstract InspectorLegalOffenseResponse entityToInspectorResponse(LegalOffenseEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inspector", source = "inspector")
    @Mapping(target = "status", constant = "NEW")
    public abstract LegalOffenseEntity requestToEntity(LegalOffenceRegisterRequest request, InspectorEntity inspector);

}
