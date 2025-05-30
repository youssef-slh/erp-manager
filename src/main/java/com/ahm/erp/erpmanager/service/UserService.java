package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.dto.Response;
import com.ahm.erp.erpmanager.dto.UserRegistrationRequest;

public interface UserService extends ResponseHandler{
    Response register(UserRegistrationRequest request);
}
