package com.ahm.erp.erpmanager.repository;

import com.ahm.erp.erpmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByKeycloakUserId(String keycloakUserId);
    boolean existsByKeycloakUserId(String keycloakUserId);
    List<User> findByOrganizationId(Integer organizationId);
}
