package com.automotive.servicemanager.repository;

import com.automotive.servicemanager.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByAssignedManager_UserId(Long serviceManagerId);
    Vehicle findVehicleByVehicleId(Long vehicleId);
}