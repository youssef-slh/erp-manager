package com.ahm.erp.erpmanager.controller;


import com.ahm.erp.erpmanager.dto.KeyValueDTO;
import com.ahm.erp.erpmanager.dto.OrganizationRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationController implements ResponseHandler {
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody OrganizationRegistrationRequest request) {
        return ResponseEntity.ok(this.organizationService.register(request));
    }

    @GetMapping
    public ResponseEntity<Response<List<KeyValueDTO>>> getOrganizations() {
        return ok(this.organizationService.getOrganizations());
    }
}
