package com.automotive.customer.repository;

import com.automotive.customer.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    List<Vehicle> findByOwner_UserId(Long customerId);

    Vehicle findVehicleByVehicleId(Long vehicleId);
}
