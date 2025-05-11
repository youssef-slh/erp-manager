package com.ahm.erp.erpmanager.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;

@Component
public class SSLConfig {

    @PostConstruct
    public void disableSSLVerification() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Allow all hostnames
        HostnameVerifier allowAllHosts = (hostname, session) -> true;

        // Set up SSL context
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCertificates, new java.security.SecureRandom());

        // Set the default context
        SSLContext.setDefault(sc);

        // Set default HostnameVerifier
        HttpsURLConnection.setDefaultHostnameVerifier(allowAllHosts);
    }
}
