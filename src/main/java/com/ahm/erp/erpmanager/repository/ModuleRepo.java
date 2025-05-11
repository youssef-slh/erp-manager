package com.ahm.erp.erpmanager.repository;

import com.ahm.erp.erpmanager.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepo extends JpaRepository<Module,Integer> {
}
