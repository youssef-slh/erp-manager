package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.KeyValueDTO;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.exception.OrganizationRegistrationException;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.OrganizationMapper;
import com.ahm.erp.erpmanager.util.JwtOrganizationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.MemberRepresentation;
import org.keycloak.representations.idm.MembershipType;
import org.keycloak.representations.idm.OrganizationDomainRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
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
        var iamOrganizationRequest = buildIAMRequest(request);
        this.keycloakIntegrationService.createOrganization(iamOrganizationRequest);
        return success();
    }

    @Override
    public List<KeyValueDTO> getOrganizations() {
        Map<String, String> organizations = JwtOrganizationUtils.extractOrganizations();

        // Convert Map to List<KeyValueDTO>
        return organizations.entrySet().stream()
                .map(entry -> new KeyValueDTO(entry.getKey(),entry.getValue()))
                .toList();
    }

    @Override
    public OrganizationRepresentation buildIAMRequest(OrganizationRegistrationRequest request) {

        String redirectURL = String.format("https://%s.com//redirect", request.companyName());

        Map<String, List<String>> additionalInfo = Map.of("org-creator-firstName", List.of(request.firstName()),
                "org-creator-lastName", List.of(request.lastName()),
                "company-icon", List.of(request.icon()),
                "company-phoneNumber", List.of(request.phoneNumber()),
                "company-email", List.of(request.email()));

        OrganizationRepresentation organizationRepresentation=new OrganizationRepresentation();

        organizationRepresentation.setName(request.companyName());
        organizationRepresentation.setAlias(request.companyName());
        organizationRepresentation.setEnabled(true);
        organizationRepresentation.setDescription(request.description());
        organizationRepresentation.setRedirectUrl(redirectURL);
        organizationRepresentation.setAttributes(additionalInfo);
        var domain = new OrganizationDomainRepresentation();
        domain.setName(request.domain());
        domain.setVerified(true);
        organizationRepresentation.addDomain(domain);

        MemberRepresentation memberRepresentation=new MemberRepresentation();

        memberRepresentation.setMembershipType(MembershipType.MANAGED);
        memberRepresentation.setUsername(request.username());
        memberRepresentation.setFirstName(request.firstName());
        memberRepresentation.setLastName(request.lastName());
        memberRepresentation.setEmailVerified(false);
        memberRepresentation.setEnabled(true);
        memberRepresentation.setEmail(request.email());
        organizationRepresentation.setMembers(List.of(memberRepresentation));
        return organizationRepresentation;
    }
}
