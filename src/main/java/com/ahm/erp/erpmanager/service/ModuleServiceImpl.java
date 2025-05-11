package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.ahm.erp.erpmanager.repository.ModuleRepo;
import com.ahm.erp.erpmanager.service.mapper.ModuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepo moduleRepo;

    @Override
    public void createModule(ModuleCreationRequest request) {
        Module module = ModuleMapper.INSTANCE.toModuleEntity(request);
        this.moduleRepo.save(module);
    }
}
