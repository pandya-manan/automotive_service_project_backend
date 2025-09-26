package com.automotive.servicemanager.repository;

import com.automotive.servicemanager.entity.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceManagerRepository extends JpaRepository<ServiceManager, Long> {
    ServiceManager findServiceManagerByUserId(Long userId);
}