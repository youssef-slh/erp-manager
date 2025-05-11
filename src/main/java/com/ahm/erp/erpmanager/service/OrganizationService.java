package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;

public interface OrganizationService extends ResponseHandler{
    Response register(OrganizationRegistrationRequest request);
    IAMOrganizationRequest buildIAMRequest(OrganizationRegistrationRequest request);
}
