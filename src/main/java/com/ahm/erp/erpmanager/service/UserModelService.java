package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.entity.UserModule;

import java.util.List;

public interface UserModelService{
    void createNew(UserModule request);
    boolean isAlreadyAssigned(Integer moduleId,String userId);
    List<UserModule> findByUserId(String userId);
    List<UserModule> findByUserIdAndActiveTrue(String userId);
}
