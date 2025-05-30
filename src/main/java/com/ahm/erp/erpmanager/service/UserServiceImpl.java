package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.IAMUserRegistrationRequest;
import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.security.KeycloakIntegrationService;
import com.ahm.erp.erpmanager.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakIntegrationService keycloakIntegrationService;

    @Override
    public Response register(UserRegistrationRequest request) {
        IAMUserRegistrationRequest iamUserRegistrationRequest = UserMapper.INSTANCE.toIAMRequest(request);
        keycloakIntegrationService.registerBackUser(iamUserRegistrationRequest);
        return success();
    }
}
