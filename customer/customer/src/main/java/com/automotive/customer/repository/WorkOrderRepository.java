package com.automotive.customer.repository;

import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder,Long> {



}
