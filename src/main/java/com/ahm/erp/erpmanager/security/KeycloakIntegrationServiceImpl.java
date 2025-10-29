package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import com.ahm.erp.erpmanager.exception.InvalidRequestException;
import com.ahm.erp.erpmanager.exception.OrganizationAlreadyExistException;
import com.ahm.erp.erpmanager.exception.UserAlreadyExistException;
import com.ahm.erp.erpmanager.util.JwtUtils;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.MemberRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakIntegrationServiceImpl implements KeycloakIntegrationService {

    private final RealmResource realmResource;

    /**
     * Create organization and link members from the request.
     */
    @Override
    public void createOrganization(OrganizationRepresentation request) {
        String orgId = createOrganizationInKeycloak(request);

        // Add members after org creation
        if (request.getMembers() != null && !request.getMembers().isEmpty()) {
            addMembersToOrganization(orgId, request.getMembers());
        }
    }

    /**
     * Create organization in Keycloak and return organization ID.
     */
    private String createOrganizationInKeycloak(OrganizationRepresentation request) {
        validateOrganizationCreation(request);
        try (Response response = realmResource.organizations().create(request)) {
            int status = response.getStatus();
            if (status != Response.Status.CREATED.getStatusCode()) {
                String errorMsg = String.format(
                        "Failed to create organization '%s'. HTTP Status: %d. Response: %s",
                        request.getName(),
                        status,
                        response.readEntity(String.class)
                );
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            String orgId = response.getHeaderString("Location").replaceAll(".*/", "");
            log.info("Organization '{}' created successfully. ID: {}", request.getName(), orgId);
            return orgId;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating organization: " + e.getMessage(), e);
        }
    }

    /**
     * Adds members to the organization. If the member does not exist, create the user first.
     */
    private void addMembersToOrganization(String orgId, List<MemberRepresentation> members) {
        for (MemberRepresentation member : members) {
            try {
                // Check if user exists in Keycloak
                findOrCreateUser(orgId, member);
            } catch (Exception e) {
                log.error("Failed to add member '{}' to organization '{}': {}",
                        member.getUsername(), orgId, e.getMessage(), e);
            }
        }
    }

    private UserRepresentation getUser(String username) {
        var users = realmResource.users().searchByUsername(username, true);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public void subscribeModuleInOrganizationAsAttribute(String orgId, Integer moduleId, String moduleName) {

        log.info("subscribing a new module [{}] in organization [{}]", moduleName, orgId);

        var orgResource = realmResource.organizations().get(orgId);
        var orgRepr = orgResource.toRepresentation();

        Map<String, List<String>> attributes = orgRepr.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        // Deserialize existing modules
        List<String> moduleJsonList = attributes.getOrDefault("modules", new ArrayList<>());

        // Create new module entry as JSON
        String moduleJson = String.format("{\"id\":%d,\"name\":\"%s\"}", moduleId, moduleName);
        moduleJsonList.add(moduleJson);

        attributes.put("module", moduleJsonList);
        orgRepr.setAttributes(attributes);

        orgResource.update(orgRepr);
    }

    /**
     * Find user by username; if not found, create it.
     */
    private String findOrCreateUser(String orgId, MemberRepresentation member) {
        // Search user by username
        var usr = getUser(member.getUsername());
        if (usr != null) {
            return usr.getId();
        }
        // Extract the user ID from Location header
        String userId = registerUser(orgId, buildUserRepresentation(member));

        // Assign admin role to the new user
        assignOrgAdminRole(userId);

        realmResource.users()
                .get(userId)
                .sendVerifyEmail();
        log.info("Member '{}' created successfully. ID: {}", member.getUsername(), userId);

        return userId;
    }

    public UserRepresentation buildUserRepresentation(MemberRepresentation member) {
        var user = new UserRepresentation();
        user.setUsername(member.getUsername());
        user.setFirstName(member.getFirstName());
        user.setLastName(member.getLastName());
        user.setEmail(member.getEmail());
        user.setEnabled(true);
        user.setRequiredActions(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"));
        user.setEmailVerified(false);
        return user;
    }

    @Override
    public String registerUser(String orgId, UserRepresentation member) {
        // User does not exist → create a UserRepresentation
        try (Response response = realmResource.users().create(member)) {
            int status = response.getStatus();
            if (status != Response.Status.CREATED.getStatusCode()) {
                String errorMsg = String.format(
                        "Failed to create member '%s'. HTTP Status: %d. Response: %s",
                        member.getUsername(),
                        status,
                        response.readEntity(String.class)
                );
                log.error(errorMsg);
                throw new IllegalStateException(errorMsg);
            }
            // Extract the user ID from Location header
            var userId = response.getHeaderString("Location").replaceAll(".*/", "");

            // Link user to organization
            realmResource.organizations()
                    .get(orgId)
                    .members()
                    .addMember(userId);

            log.info("Member '{}' linked to organization '{}'.", member.getUsername(), orgId);

            return userId;
        }
    }

    @Override
    public void validName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRequestException();
        }

        var org = realmResource.organizations().search(name);
        if (!org.isEmpty()) {
            throw new OrganizationAlreadyExistException();
        }
    }

    @Override
    public void validDomain(String domain) {
        if (domain == null && !domain.trim().isEmpty()) {
            throw new InvalidRequestException();
        }

        var org = realmResource.organizations().search(domain);
        if (!org.isEmpty()) {
            throw new OrganizationAlreadyExistException();
        }
    }

    @Override
    public void validateOrganizationCreation(OrganizationRepresentation request) {
        //validate domain;
        validDomain(request.getName());
        validName(request.getDomains().stream().findFirst().get().getName());

        var username = getUser(request.getMembers().get(0).getUsername());
        if (username != null) {
            throw new UserAlreadyExistException();
        }

        var email = realmResource.users().searchByEmail(request.getMembers().get(0).getEmail(), true);
        if (!email.isEmpty()) {
            throw new UserAlreadyExistException();
        }

    }

    @Override
    public void registerBackUser(IAMUserRegistrationRequest request) {
        // Implement registration via your API or Keycloak if needed
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void LinkUserToOrganization(String userId, String orgId) {
        try {
            realmResource.organizations()
                    .get(orgId)
                    .members()
                    .addMember(userId);
            log.info("User '{}' linked to organization '{}'", userId, orgId);
        } catch (Exception e) {
            log.error("Failed to link user '{}' to organization '{}': {}", userId, orgId, e.getMessage(), e);
        }
    }


    private void assignOrgAdminRole(String userId) {
        // Ensure the ORG_ADMIN role exists in your realm
        var adminRole = realmResource.roles().get("ORG_ADMIN").toRepresentation();

        // Assign to user
        realmResource.users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(adminRole));

        log.info("ORG_ADMIN role assigned to user {}", userId);
    }

    @Override
    public OrganizationRepresentation getOrganizationRepresentationById(String orgId) {
        var organization = this.realmResource.organizations()
                .get(orgId)
                .toRepresentation();
        boolean enabled = organization.isEnabled();
        if (!enabled) {
            throw new InvalidRequestException();
        }

        return organization;

    }

    @Override
    public OrganizationResource getOrganizationResourceById(String orgId) {
        return this.realmResource.organizations()
                .get(orgId);
    }

    @Override
    public boolean isMemberInOrganization(String orgId) {

        getOrganizationRepresentationById(orgId);
        Map<String, String> organizations = JwtUtils.extractOrganizations();

        return organizations.keySet()
                .stream()
                .anyMatch(s -> s.equals(orgId));
    }
}
