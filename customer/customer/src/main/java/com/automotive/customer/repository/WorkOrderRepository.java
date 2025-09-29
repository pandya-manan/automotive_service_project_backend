package com.automotive.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.automotive.customer.entity.ServiceStatusProjection;
import com.automotive.customer.entity.WorkOrder;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder,Long> {


	@Query(value = """
	        SELECT 
	            v.registration_number AS registrationNumber,
	            v.is_booked_for_service AS bookingStatus,
	            v.is_service_done AS serviceCompleted,
	            w.scheduled_at AS serviceBookingDate,
	            w.status AS serviceStatus,
	            w.assigned_by_manager_id AS serviceManager,
	            w.mechanic_id AS mechanicAssigned
	        FROM vehicles v
	        INNER JOIN work_orders w ON w.vehicle_id = v.vehicle_id
	        INNER JOIN user u ON u.user_id = v.user_id
	        WHERE u.user_id = :userId
	        ORDER BY w.scheduled_at DESC
	        """, nativeQuery = true)
	    List<ServiceStatusProjection> findServiceStatusNative(@Param("userId") Long userId);

}
