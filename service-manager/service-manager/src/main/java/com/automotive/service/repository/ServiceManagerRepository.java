package com.automotive.service.repository;

import com.automotive.service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceManagerRepository extends JpaRepository<ServiceManager, Long> {
}
