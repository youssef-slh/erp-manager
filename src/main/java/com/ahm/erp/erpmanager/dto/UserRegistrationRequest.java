package com.ahm.erp.erpmanager.dto;

public record UserRegistrationRequest(String firstName,
                                      String lastName,
                                      String username,
                                      String email) {
}
