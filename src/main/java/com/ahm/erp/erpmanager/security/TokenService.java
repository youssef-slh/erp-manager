package com.ahm.erp.erpmanager.security;

import com.ahm.erp.erpmanager.configuration.SecurityProperties;
import com.nimbusds.oauth2.sdk.ClientCredentialsGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final SecurityProperties securityProperties;

    public TokenResponse token() {

        try {
            ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant();

            // The credentials to authenticate the client at the token endpoint
            ClientID clientID = new ClientID(securityProperties.getClientId());
            Secret clientSecret = new Secret(securityProperties.getClientSecret());
            ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);

            // Make the token request
            TokenRequest request = new TokenRequest(URI.create(securityProperties.getIamTokenHost()),
                    clientAuth, clientCredentialsGrant,
                    null,
                    null,
                    null);

            return TokenResponse.parse(request.toHTTPRequest().send());
        } catch (IOException e) {
            throw new RuntimeException("Failed to call identity server", e);
        } catch (ParseException p) {
            throw new RuntimeException("Failed to parse incoming token from identity server", p);
        }
    }

}
