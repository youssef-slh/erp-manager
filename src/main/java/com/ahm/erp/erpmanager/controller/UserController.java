package com.ahm.erp.erpmanager.controller;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ahm.erp.erpmanager.dto.UserModuleAssignmentRequest;
import com.ahm.erp.erpmanager.entity.Module;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.List;
import com.ahm.erp.erpmanager.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody UserRegistrationRequest request) {
       return ResponseEntity.ok(this.userService.register(request));
    }

    @PostMapping("/invite")
    public ResponseEntity<Response> inviteUSerToOrganization(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(this.userService.register(request));
    }

    @PostMapping("/{keycloakUserId}/organizations/{organizationId}/modules")
    public ResponseEntity<Response> assignModulesToUser(@PathVariable String keycloakUserId,
                                                      @PathVariable Integer organizationId,
                                                      @RequestBody UserModuleAssignmentRequest request) {
        return ResponseEntity.ok(userService.assignModulesToUser(keycloakUserId, organizationId, request));
    }

    @GetMapping("/{keycloakUserId}/organizations/{organizationId}/modules")
    public ResponseEntity<Set<Module>> getUserModules(@PathVariable String keycloakUserId,
                                                      @PathVariable Integer organizationId) {
        return ResponseEntity.ok(userService.getUserModules(keycloakUserId, organizationId));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<User>> getUsersByOrganization(@PathVariable Integer organizationId) {
        return ResponseEntity.ok(userService.getUsersByOrganization(organizationId));
    }
}
