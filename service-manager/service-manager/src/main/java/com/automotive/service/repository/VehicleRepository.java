package com.automotive.service.repository;

import com.automotive.service.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleId(Long vehicleId);
}
