package com.automotive.servicemanager.service;

import com.automotive.servicemanager.entity.Vehicle;
import com.automotive.servicemanager.entity.WorkOrder;
import com.automotive.servicemanager.exception.ServiceManagerDoesNotExistException;
import com.automotive.servicemanager.exception.VehicleDoesNotExistException;
import com.automotive.servicemanager.exception.WorkOrderDoesNotExistException;

import java.util.List;

public interface ServiceManagerService {
    List<Vehicle> getAssignedVehicles(Long serviceManagerId) throws ServiceManagerDoesNotExistException;
    List<WorkOrder> getWorkOrdersForManager(Long serviceManagerId) throws ServiceManagerDoesNotExistException;
    WorkOrder updateWorkOrderStatus(Long serviceManagerId, Long workOrderId, String status)
            throws ServiceManagerDoesNotExistException, WorkOrderDoesNotExistException;
    void unassignVehicle(Long serviceManagerId, Long vehicleId)
            throws ServiceManagerDoesNotExistException, VehicleDoesNotExistException;
}