package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.ahm.erp.erpmanager.entity.UserModule;
import com.ahm.erp.erpmanager.exception.ModuleNotFoundException;
import com.ahm.erp.erpmanager.repository.ModuleRepo;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.ModuleMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepo moduleRepo;
    private final KeycloakIntegrationService keycloakIntegrationService;
    private final ObjectMapper objectMapper;

    @Override
    public void createModule(ModuleCreationRequest request) {
        Module module = ModuleMapper.INSTANCE.toModuleEntity(request);
        this.moduleRepo.save(module);
    }

    @Override
    public List<Module> retrieveAll() {
        log.info("retrieving all active modules from db");
        return this.moduleRepo.findAll()
                .stream()
                .filter(Module::isActive)
                .toList();
    }

    @Override
    public List<Module> retrieveSubscribedModules(String organizationId) {
        log.info("retrieving subscribed modules for organization with id [{}]", organizationId);
        var orgRepr = this.keycloakIntegrationService.getOrganizationRepresentationById(organizationId);
        var modulesId = orgRepr.getAttributes().getOrDefault("module", List.of())
                .stream()
                .map(moduleJson -> {
                    try {
                        return objectMapper.readValue(moduleJson, Module.class);
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing module JSON: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(Module::getId)
                .toList();

        return this.moduleRepo.findAllById(modulesId);
    }

    @Override
    public void subscribe(String organizationId, Integer moduleId) throws JsonProcessingException {
        log.info("subscribing module with id [{}] to organization with id [{}]", moduleId, organizationId);
        Module module = this.fetchById(moduleId);
        this.keycloakIntegrationService.subscribeModuleInOrganizationAsAttribute(
                organizationId,
                moduleId,
                module.getName()
        );
    }


    @Override
    public Module fetchById(Integer id) {
        log.info("retrieving module from db with id [{}]", id);
        return this.moduleRepo.findById(id)
                .orElseThrow(ModuleNotFoundException::new);
    }
}
