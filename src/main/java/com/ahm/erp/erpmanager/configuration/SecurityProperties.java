package com.ahm.erp.erpmanager.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private String clientId;
    private String clientSecret;
    private String iamTokenHost;
}
