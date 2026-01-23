package com.uzumtech.finespenalties.mapper;


import com.uzumtech.finespenalties.dto.request.UserRegisterRequest;
import com.uzumtech.finespenalties.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encryptedPassword")
    UserEntity mapRegisterRequest(UserRegisterRequest request, String encryptedPassword);

}
