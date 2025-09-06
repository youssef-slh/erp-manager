package com.ahm.erp.erpmanager.repository;

import com.ahm.erp.erpmanager.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepo extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByCompanyName(String companyName);
    boolean existsByCompanyName(String companyName);
}
