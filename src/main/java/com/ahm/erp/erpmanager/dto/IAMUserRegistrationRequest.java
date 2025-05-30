package com.ahm.erp.erpmanager.dto;

public record IAMUserRegistrationRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        boolean enabled,
        boolean emailVerified
) {
}