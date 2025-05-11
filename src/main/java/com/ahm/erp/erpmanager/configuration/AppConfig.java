package com.ahm.erp.erpmanager.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean("keycloakRestTemplate")
    public RestTemplate keycloakRestTemplate(RestTemplateBuilder restTemplateBuilder) throws NoSuchAlgorithmException {
               return restTemplateBuilder
                .connectTimeout(Duration.ofMinutes(2))
                .readTimeout(Duration.ofMinutes(2))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
