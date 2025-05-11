package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.ModuleCreationRequest;
import com.ahm.erp.erpmanager.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/module")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity createModule(@RequestBody ModuleCreationRequest request) {
        this.moduleService.createModule(request);
        return ResponseEntity.noContent().build();
    }
}
