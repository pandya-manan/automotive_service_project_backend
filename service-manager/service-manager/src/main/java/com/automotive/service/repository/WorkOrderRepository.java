package com.automotive.service.repository;

import com.automotive.service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByServiceOrderId(String serviceOrderId);
    List<WorkOrder> findByStatus(WorkOrderStatus status);
}
