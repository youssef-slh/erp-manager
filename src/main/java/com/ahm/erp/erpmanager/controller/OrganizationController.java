package com.ahm.erp.erpmanager.controller;


import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody OrganizationRegistrationRequest request) {
       return ResponseEntity.ok(this.organizationService.register(request));
    }
}
