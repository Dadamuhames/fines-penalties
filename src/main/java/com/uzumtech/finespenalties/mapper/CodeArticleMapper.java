package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.response.CodeArticleDto;
import com.uzumtech.finespenalties.dto.response.CodeArticleResponse;
import com.uzumtech.finespenalties.entity.CodeArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CodeArticleMapper {


    CodeArticleResponse entityToResponse(CodeArticleEntity entity);

    CodeArticleDto entityToDto(CodeArticleEntity entity);

}
