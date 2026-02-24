package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.response.GcpResponse;
import com.uzumtech.finespenalties.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullName", expression = "java(concatName(gcpResponse))")
    @Mapping(target = "pinfl", source = "gcpResponse.personalIdentificationNumber")
    @Mapping(target = "phone", source = "gcpResponse.phoneNumber")
    UserEntity gcpResponseToUserEntity(GcpResponse gcpResponse);

    default String concatName(GcpResponse gcpResponse) {
        return String.format("%s %s", gcpResponse.name(), gcpResponse.surname());
    }
}
