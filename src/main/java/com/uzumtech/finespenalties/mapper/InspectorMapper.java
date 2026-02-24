package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.response.InspectorDto;
import com.uzumtech.finespenalties.entity.InspectorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InspectorMapper {

    InspectorDto inspectorToDto(InspectorEntity inspector);

}
