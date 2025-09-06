package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.UserMapper;
import com.ahm.erp.erpmanager.util.JwtOrganizationUtils;
import com.ahm.erp.erpmanager.repository.UserRepo;
import com.ahm.erp.erpmanager.repository.ModuleRepo;
import com.ahm.erp.erpmanager.dto.UserModuleAssignmentRequest;
import com.ahm.erp.erpmanager.entity.User;
import com.ahm.erp.erpmanager.entity.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MemberRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakIntegrationService keycloakIntegrationService;
    private final UserRepo userRepo;
    private final ModuleRepo moduleRepo;

    @Override
    public Response register(UserRegistrationRequest request) {

        Map<String, String> organizations = JwtOrganizationUtils.extractOrganizations();

        organizations.forEach((id, name) -> {
            var iamRequest = UserMapper.INSTANCE.toIAMRequest(request);
            var cred = new CredentialRepresentation();
            cred.setTemporary(true);
            cred.setValue("123456"); // TODO: Generate a strong temporary password and send to user
            iamRequest.setCredentials(List.of(cred));
            log.info("Registering user for organization: {} (id={})", name, id);
            this.keycloakIntegrationService.registerUser(id, iamRequest);

            // Create a local User entity
            User user = new User();
            user.setKeycloakUserId(request.username()); // Assuming username is used as keycloakUserId for simplicity
            user.setOrganizationId(request.organizationId());
            userRepo.save(user);
        });

        return success();
    }

    @Override
    public boolean isAlreadyExists(String username) {
        return userRepo.existsByKeycloakUserId(username);
    }

    @Override
    public Response assignModulesToUser(String keycloakUserId, Integer organizationId, UserModuleAssignmentRequest request) {
        User user = userRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("User not found")); // TODO: Custom exception

        if (!user.getOrganizationId().equals(organizationId)) {
            throw new RuntimeException("User does not belong to the specified organization"); // TODO: Custom exception
        }

        Set<Module> modulesToAssign = request.moduleIds().stream()
                .map(moduleRepo::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setModules(modulesToAssign);
        userRepo.save(user);

        return success();
    }

    @Override
    public Set<Module> getUserModules(String keycloakUserId, Integer organizationId) {
        User user = userRepo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("User not found")); // TODO: Custom exception

        if (!user.getOrganizationId().equals(organizationId)) {
            throw new RuntimeException("User does not belong to the specified organization"); // TODO: Custom exception
        }

        return user.getModules();
    }

    @Override
    public List<User> getUsersByOrganization(Integer organizationId) {
        return userRepo.findByOrganizationId(organizationId);
    }
}