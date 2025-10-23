package com.automotive.mechanic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automotive.mechanic.entity.WorkOrder;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByServiceOrderIdAndMechanic_UserId(String serviceOrderId, Long mechanicId);
    List<WorkOrder> findByMechanic_UserId(Long mechanicId);
}
