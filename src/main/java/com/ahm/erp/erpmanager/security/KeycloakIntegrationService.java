package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.representations.idm.MemberRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakIntegrationService {

    void createOrganization(OrganizationRepresentation request);

    void registerBackUser(IAMUserRegistrationRequest request);

    void LinkUserToOrganization(String userId, String orgId);

    void validName(String name);

    void validDomain(String domain);

    void validateOrganizationCreation(OrganizationRepresentation request);

    String registerUser(String orgId, UserRepresentation member);

    void subscribeModuleInOrganizationAsAttribute(String orgId, Integer moduleId, String moduleName);

    OrganizationRepresentation getOrganizationRepresentationById(String orgId);

    OrganizationResource getOrganizationResourceById(String orgId);

    boolean isMemberInOrganization(String orgId);


}
