package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.configuration.AppProperties;
import com.ahm.erp.erpmanager.dto.IAMOrganizationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
@RequiredArgsConstructor
public class KeycloakIntegrationServiceImpl implements KeycloakIntegrationService {

    private final TokenService tokenService;
    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;



    @Override
    public void createOrganization(IAMOrganizationRequest request) {
        try {
            var token = this.tokenService.token();
            if (token.indicatesSuccess()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token.toSuccessResponse().getTokens().getAccessToken().getValue()));
                HttpClient client = HttpClient.newBuilder().sslContext(SSLContext.getInstance("SSL")).build();
                HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(this.appProperties.getIamHost().concat("/organizations")))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token.toSuccessResponse().getTokens().getAccessToken().getValue()))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(request)))
                        .build();
                client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
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
}
