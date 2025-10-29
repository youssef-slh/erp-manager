package com.ahm.erp.erpmanager.service;

import com.ahm.erp.erpmanager.entity.UserModule;
import com.ahm.erp.erpmanager.repository.UserModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserModelServiceImpl implements UserModelService {
    private final UserModuleRepository userModuleRepository;

    @Override
    public void createNew(UserModule request) {
        log.info("assigning user [{}] to module with id[{}]", request.getUserId(), request.getModule().getId());
        this.userModuleRepository.save(request);
    }

    @Override
    public boolean isAlreadyAssigned(Integer moduleId, String userId) {
        Optional<UserModule> userModule = this.userModuleRepository.findByUserIdAndModuleActiveTrue(userId, moduleId);
        return userModule.isPresent();
    }

    @Override
    public List<UserModule> findByUserId(String userId) {
        log.info("retrieving modules for user with id [{}]", userId);
        return this.userModuleRepository.findByUserId(userId);
    }

    @Override
    public List<UserModule> findByUserIdAndActiveTrue(String userId) {
        log.info("retrieving active modules for user with id [{}]", userId);
        return this.userModuleRepository.findByUserIdAndActiveTrue(userId);
    }
}
