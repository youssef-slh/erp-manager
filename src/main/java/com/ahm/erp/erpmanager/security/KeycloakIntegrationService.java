package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;

public interface KeycloakIntegrationService {

    void createOrganization(IAMOrganizationRequest request);
    void registerBackUser(IAMUserRegistrationRequest request);
    void LinkUserToOrganization(String userId,String orgId);
    boolean validName(String name);
    boolean validDomain(String domain);
}
