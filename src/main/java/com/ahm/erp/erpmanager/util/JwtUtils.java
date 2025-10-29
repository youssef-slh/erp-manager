package com.ahm.erp.erpmanager.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.LinkedHashMap;
import java.util.Map;

public class JwtUtils {

    /**
     * Extracts a map of organization name -> organization id from the JWT token.
     *
     * @return Map of organization name to id
     */
    public static Map<String, String> extractOrganizations() {

        Object orgClaim = extractClaimAttribute("organization");

        Map<String, String> organizations = new LinkedHashMap<>();

        if (orgClaim instanceof Map<?, ?> orgMap) {
            orgMap.forEach((nameObj, valueObj) -> {
                String orgName = nameObj.toString();
                if (valueObj instanceof Map<?, ?> innerMap) {
                    Object idObj = innerMap.get("id");
                    if (idObj != null) {
                        organizations.put(idObj.toString(), orgName);
                    }
                }
            });
        }

        if (organizations.isEmpty()) {
            throw new IllegalStateException("No organizations found in JWT");
        }
        return organizations;
    }


    public static Object extractClaimAttribute(String name) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("JWT authentication is required");
        }
        Object object = jwtAuth.getTokenAttributes().get(name);
        if (object == null) {
            throw new IllegalStateException("No " + name + " found in JWT");
        }
        return object;
    }
}
