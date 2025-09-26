package com.automotive.servicemanager.repository;

import com.automotive.servicemanager.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByAssignedBy_UserId(Long serviceManagerId);
}