package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.service.ModuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ahm.erp.erpmanager.entity.Module;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/modules")
public class ModuleController implements ResponseHandler {

    private final ModuleService moduleService;

    @PostMapping
    @PreAuthorize("hasRole('MODULE_MANAGEMENT')")
    public ResponseEntity createModule(@RequestBody ModuleCreationRequest request) {
        this.moduleService.createModule(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Response<List<Module>>> retrieveList() {
        return ok(this.moduleService.retrieveAll());
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Response<List<Module>>> retrieveSubscribedModules(@PathVariable String organizationId) {
        return ok(this.moduleService.retrieveSubscribedModules(organizationId));
    }

    @PostMapping("/subscribe/{organizationId}/{moduleId}")
    public ResponseEntity subscribe(@PathVariable String organizationId, @PathVariable Integer moduleId) throws JsonProcessingException {
        this.moduleService.subscribe(organizationId, moduleId);
        return ResponseEntity.noContent().build();
    }

}
