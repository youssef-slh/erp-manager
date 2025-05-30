package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakIntegrationServiceImpl implements KeycloakIntegrationService {

    private final TokenService tokenService;
    private final WebClient webClient;

    @Override
    public void createOrganization(IAMOrganizationRequest request) {
        try {
            var token = this.tokenService.token();
            if (token.indicatesSuccess()) {
                webClient.post()
                        .uri("/organizations")
                        .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token.toSuccessResponse().getTokens().getAccessToken().getValue()))
                        .bodyValue(request)
                        .retrieve()
                        .toBodilessEntity()
                        .doOnError(throwable -> {
                            log.error(throwable.getMessage());
                        })
                        .block();
            }
        } catch (Exception exception) {
            throw new RuntimeException();
        }

    }

    @Override
    public boolean validName(String name) {
        return false;
    }

    @Override
    public boolean validDomain(String domain) {
        return false;
    }

    private boolean isOrganizationFound(String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return false;
    }

    @Override
    public void registerBackUser(IAMUserRegistrationRequest request) {
        try {
            var token = this.tokenService.token();
            if (token.indicatesSuccess()) {
                webClient.post()
                        .uri("/users")
                        .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token.toSuccessResponse().getTokens().getAccessToken().getValue()))
                        .bodyValue(request)
                        .retrieve()
                        .toBodilessEntity()
                        .doOnError(throwable -> {
                            log.error(throwable.getMessage());
                        })
                        .block();
            }
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }

    @Override
    public void LinkUserToOrganization(String userId, String orgId) {

    }
}
