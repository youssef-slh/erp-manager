package com.ahm.erp.erpmanager.dto;

import java.util.List;
import java.util.Map;

public record IAMOrganizationRequest(
        String name,
        String alias,
        boolean enabled,
        String description,
        String redirectUrl,
        Map<String, List<String>> attributes,
        List<Domain> domains
) {
    public record Domain(String name, boolean verified) {
    }
}