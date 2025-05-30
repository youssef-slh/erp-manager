package com.ahm.erp.erpmanager.service.mapper;

import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "emailVerified", constant = "true")
    IAMUserRegistrationRequest toIAMRequest(UserRegistrationRequest request);
}