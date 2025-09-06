package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.KeyValueDTO;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import org.keycloak.representations.idm.OrganizationRepresentation;

import java.util.List;

public interface OrganizationService extends ResponseHandler{
    Response register(OrganizationRegistrationRequest request);
    List<KeyValueDTO> getOrganizations();
    OrganizationRepresentation buildIAMRequest(OrganizationRegistrationRequest request);
}
