package com.automotive.customer.service;

import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;

import java.util.List;
public interface VehicleService {

    public Vehicle addVehicleToCustomer(Long customerId, Vehicle vehicle) throws CustomerDoesNotExistException;

    public List<Vehicle> getAllVehiclesForCustomer(Long customerId);

    public Vehicle updateVehicle(Long vehicleId,Vehicle updatedVehicle) throws CustomerDoesNotExistException, VehicleDoesNotExistException;

    public void deleteVehicle(Long vehicleId) throws VehicleDoesNotExistException;

}
