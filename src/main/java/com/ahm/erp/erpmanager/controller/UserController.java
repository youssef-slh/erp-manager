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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody UserRegistrationRequest request) {
       return ResponseEntity.ok(this.userService.register(request));
    }
}
