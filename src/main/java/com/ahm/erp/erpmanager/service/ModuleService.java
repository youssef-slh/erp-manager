package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ModuleService {
    void createModule(ModuleCreationRequest request);

    List<Module> retrieveAll();

    List<Module> retrieveSubscribedModules(String organizationId);

    void subscribe(String organizationId, Integer moduleId) throws JsonProcessingException;

    Module fetchById(Integer id);
}
