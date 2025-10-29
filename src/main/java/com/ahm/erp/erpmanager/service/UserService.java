package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserDTO;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface UserService extends ResponseHandler {
    Response register(UserRegistrationRequest request);

    boolean isAlreadyExists(String username);

    List<UserDTO> retrieveUsers(String orgId);

    List<Module> retrieveUserModules(String userId);

    Response assignUser(String userId, Integer moduleId) throws JsonProcessingException;
}
