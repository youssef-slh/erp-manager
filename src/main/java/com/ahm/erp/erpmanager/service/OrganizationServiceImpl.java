package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.exception.OrganizationRegistrationException;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.OrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final KeycloakIntegrationService keycloakIntegrationService;

    @Override
    public Response register(OrganizationRegistrationRequest request) {
        IAMOrganizationRequest iamOrganizationRequest = buildIAMRequest(request);
        this.keycloakIntegrationService.createOrganization(iamOrganizationRequest);
        return success();
    }

    @Override
    public IAMOrganizationRequest buildIAMRequest(OrganizationRegistrationRequest request) {

        String redirectURL = String.format("https://%s.com//redirect", request.companyName());

        Map<String, List<String>> additionalInfo = Map.of("org-creator-firstName", List.of(request.firstName()),
                "org-creator-lastName", List.of(request.lastName()),
                "company-icon", List.of(request.icon()),
                "company-phoneNumber", List.of(request.phoneNumber()),
                "company-email", List.of(request.email()));

        IAMOrganizationRequest.Domain domain = new IAMOrganizationRequest.Domain(request.domain(), true);
        return new IAMOrganizationRequest(request.companyName(), request.companyName(),
                true, request.description(), redirectURL, additionalInfo, List.of(domain));
    }
}
