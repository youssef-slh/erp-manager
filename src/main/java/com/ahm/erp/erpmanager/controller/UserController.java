package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserDTO;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ahm.erp.erpmanager.entity.Module;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController implements ResponseHandler {
    private final UserService userService;

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Response<List<UserDTO>>> getOrganizationUsers(@PathVariable String organizationId) {
        return ok(this.userService.retrieveUsers(organizationId));
    }

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(this.userService.register(request));
    }

    @PostMapping("/{keycloakUserId}/module/{moduleId}")
    public ResponseEntity<Response> assignModulesToUser(@PathVariable String keycloakUserId,
                                                        @PathVariable Integer moduleId) throws JsonProcessingException {
        return ResponseEntity.ok(this.userService.assignUser(keycloakUserId, moduleId));
    }

    @GetMapping("/{keycloakUserId}/modules")
    public ResponseEntity<Response<List<Module>>> getUserModules(@PathVariable String keycloakUserId) {
        return ok(this.userService.retrieveUserModules(keycloakUserId));
    }
}
