package com.ahm.erp.erpmanager.dto;

public record OrganizationRegistrationRequest(String username,String firstName, String lastName, String companyName, String icon,
                                              String description, String email, String phoneNumber,
                                              String alias, String domain) {

}
