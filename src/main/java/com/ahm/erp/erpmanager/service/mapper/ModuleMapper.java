package com.ahm.erp.erpmanager.service.mapper;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ModuleMapper {
    ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

    Module toModuleEntity(ModuleCreationRequest request);
}