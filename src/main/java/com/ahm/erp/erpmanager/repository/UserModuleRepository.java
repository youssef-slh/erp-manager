package com.ahm.erp.erpmanager.repository;

import com.ahm.erp.erpmanager.entity.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Long> {

    List<UserModule> findByUserId(String userId);

    List<UserModule> findByModuleId(Integer moduleId);

    List<UserModule> findByUserIdAndActiveTrue(String userId);

    @Query("""
            select u from UserModule u
            join u.module um
            where u.userId= :userId
            and um.id= :moduleId
            and u.active= true
            """)
    Optional<UserModule> findByUserIdAndModuleActiveTrue(String userId, Integer moduleId);
}
