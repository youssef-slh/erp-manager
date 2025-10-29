package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserDTO;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.ahm.erp.erpmanager.entity.UserModule;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.UserMapper;
import com.ahm.erp.erpmanager.util.GeneralHelper;
import com.ahm.erp.erpmanager.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jwt.JWTClaimNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakIntegrationService keycloakIntegrationService;
    private final ModuleService moduleService;
    private final UserModelService userModelService;

    @Override
    public Response register(UserRegistrationRequest request) {

        Map<String, String> organizations = JwtUtils.extractOrganizations();

        organizations.forEach((id, name) -> {
            var iamRequest = UserMapper.INSTANCE.toIAMRequest(request);
            var cred = new CredentialRepresentation();
            cred.setTemporary(true);
            cred.setValue("123456"); // TODO: Generate a strong temporary password and send to user
            iamRequest.setCredentials(List.of(cred));
            log.info("Registering user for organization: {} (id={})", name, id);
            this.keycloakIntegrationService.registerUser(id, iamRequest);
        });

        return success();
    }

    @Override
    public boolean isAlreadyExists(String username) {
        return false; // Original behavior before UserRepo was introduced
    }

    @Override
    public List<UserDTO> retrieveUsers(String orgId) {
        log.info("retrieving users for organization [{}]", orgId);
        var organizationResource = this.keycloakIntegrationService.getOrganizationResourceById(orgId);
        var members = organizationResource.members().list(0, 999);
        var id = (String) JwtUtils.extractClaimAttribute(JWTClaimNames.SUBJECT);
        return members.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getEmail(),
                        GeneralHelper.toLocalDateTime(user.getCreatedTimestamp())
                ))
                .filter(userDTO -> !userDTO.id().equals(id))
                .toList();
    }

    @Override
    public List<Module> retrieveUserModules(String userId) {
        var userModules = this.userModelService.findByUserIdAndActiveTrue(userId);
        return userModules.stream()
                .map(UserModule::getModule)
                .toList();
    }

    @Override
    public Response assignUser(String userId, Integer moduleId) {
        var module = this.moduleService.fetchById(moduleId);

        boolean alreadyAssigned = this.userModelService.isAlreadyAssigned(moduleId, userId);

        if (alreadyAssigned) {
            return success();
        }

        var userModule = new UserModule();
        userModule.setUserId(userId);
        userModule.setModule(module);
        userModule.setActive(true);
        this.userModelService.createNew(userModule);
        return success();
    }
}