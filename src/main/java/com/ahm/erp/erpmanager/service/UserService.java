package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;
import com.ahm.erp.erpmanager.dto.UserModuleAssignmentRequest;
import com.ahm.erp.erpmanager.entity.Module;
import com.ahm.erp.erpmanager.entity.User;

import java.util.Set;
import java.util.List;

public interface UserService extends ResponseHandler{
    Response register(UserRegistrationRequest request);
    boolean isAlreadyExists(String username);
    Response assignModulesToUser(String keycloakUserId, Integer organizationId, UserModuleAssignmentRequest request);
    Set<Module> getUserModules(String keycloakUserId, Integer organizationId);
    List<User> getUsersByOrganization(Integer organizationId);
}
