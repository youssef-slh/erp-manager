package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ahm.erp.erpmanager.dto.ModuleSubscriptionRequest;

import java.util.List;
import java.util.Optional;

public interface ModuleService {
    void createModule(ModuleCreationRequest request);

    List<Module> retrieveAll();

    List<Module> retrieveSubscribedModules(Integer organizationId);

    void subscribe(ModuleSubscriptionRequest request);

    Module fetchById(Long id);
}
