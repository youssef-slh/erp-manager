package com.ahm.erp.erpmanager.service.mapper;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);


    IAMOrganizationRequest toIAMRequest(OrganizationRegistrationRequest request);
}