package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.KeyValueDTO;
import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.dto.ModuleSubscriptionRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.ahm.erp.erpmanager.entity.Organization;
import com.ahm.erp.erpmanager.exception.InvalidRequestException;
import com.ahm.erp.erpmanager.exception.ModuleNotFoundException;
import com.ahm.erp.erpmanager.repository.ModuleRepo;
import com.ahm.erp.erpmanager.repository.OrganizationRepo;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.ModuleMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepo moduleRepo;
    private final KeycloakIntegrationService keycloakIntegrationService;
    private final ObjectMapper objectMapper;
    private final OrganizationRepo organizationRepo;

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
    public List<Module> retrieveSubscribedModules(Integer organizationId) {
        Organization organization = organizationRepo.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found")); // TODO: Custom exception

        return new ArrayList<>(organization.getModules());
    }

    @Override
    public void subscribe(ModuleSubscriptionRequest request) {
        Organization organization = organizationRepo.findById(request.organizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found")); // TODO: Custom exception

        Module module = moduleRepo.findById(request.moduleId())
                .orElseThrow(() -> new ModuleNotFoundException());

        organization.getModules().add(module);
        module.getOrganizations().add(organization);

        organizationRepo.save(organization);
        moduleRepo.save(module);
    }

    @Override
    public Module fetchById(Long id) {
        log.info("retrieving module from db with id [{}]", id);
        return this.moduleRepo.findById(id.intValue())
                .orElseThrow(ModuleNotFoundException::new);
    }
}
