package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;

public interface KeycloakIntegrationService {

    void createOrganization(IAMOrganizationRequest request);
    boolean validName(String name);
    boolean validDomain(String domain);
}
