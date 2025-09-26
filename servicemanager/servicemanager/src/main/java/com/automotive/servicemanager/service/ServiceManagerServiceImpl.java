package com.automotive.servicemanager.service;

import com.automotive.servicemanager.entity.ServiceManager;
import com.automotive.servicemanager.entity.Vehicle;
import com.automotive.servicemanager.entity.WorkOrder;
import com.automotive.servicemanager.entity.WorkOrderStatus;
import com.automotive.servicemanager.exception.ServiceManagerDoesNotExistException;
import com.automotive.servicemanager.exception.VehicleDoesNotExistException;
import com.automotive.servicemanager.exception.WorkOrderDoesNotExistException;
import com.automotive.servicemanager.repository.ServiceManagerRepository;
import com.automotive.servicemanager.repository.VehicleRepository;
import com.automotive.servicemanager.repository.WorkOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ServiceManagerServiceImpl implements ServiceManagerService {

    private static final Logger logger = Logger.getLogger(ServiceManagerServiceImpl.class.getName());

    @Autowired
    private ServiceManagerRepository serviceManagerRepo;

    @Autowired
    private VehicleRepository vehicleRepo;

    @Autowired
    private WorkOrderRepository workOrderRepo;

    @Override
    public List<Vehicle> getAssignedVehicles(Long serviceManagerId) throws ServiceManagerDoesNotExistException {
        logger.info("Fetching vehicles for Service Manager ID: " + serviceManagerId);
        ServiceManager manager = serviceManagerRepo.findServiceManagerByUserId(serviceManagerId);
        if (manager == null) {
            logger.warning("Service Manager with ID " + serviceManagerId + " does not exist");
            throw new ServiceManagerDoesNotExistException("Service Manager with ID " + serviceManagerId + " does not exist");
        }
        List<Vehicle> vehicles = vehicleRepo.findByAssignedManager_UserId(serviceManagerId);
        logger.info("Found " + vehicles.size() + " vehicles for Service Manager ID: " + serviceManagerId);
        return vehicles;
    }

    @Override
    public List<WorkOrder> getWorkOrdersForManager(Long serviceManagerId) throws ServiceManagerDoesNotExistException {
        logger.info("Fetching work orders for Service Manager ID: " + serviceManagerId);
        ServiceManager manager = serviceManagerRepo.findServiceManagerByUserId(serviceManagerId);
        if (manager == null) {
            logger.warning("Service Manager with ID " + serviceManagerId + " does not exist");
            throw new ServiceManagerDoesNotExistException("Service Manager with ID " + serviceManagerId + " does not exist");
        }
        List<WorkOrder> workOrders = workOrderRepo.findByAssignedBy_UserId(serviceManagerId);
        logger.info("Found " + workOrders.size() + " work orders for Service Manager ID: " + serviceManagerId);
        return workOrders;
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrderStatus(Long serviceManagerId, Long workOrderId, String status)
            throws ServiceManagerDoesNotExistException, WorkOrderDoesNotExistException {
        logger.info("Updating work order status for Work Order ID: " + workOrderId + " by Service Manager ID: " + serviceManagerId);
        ServiceManager manager = serviceManagerRepo.findServiceManagerByUserId(serviceManagerId);
        if (manager == null) {
            logger.warning("Service Manager with ID " + serviceManagerId + " does not exist");
            throw new ServiceManagerDoesNotExistException("Service Manager with ID " + serviceManagerId + " does not exist");
        }

        WorkOrder workOrder = workOrderRepo.findById(workOrderId)
                .orElseThrow(() -> {
                    logger.warning("Work Order with ID " + workOrderId + " does not exist");
                    return new WorkOrderDoesNotExistException("Work Order with ID " + workOrderId + " does not exist");
                });

        WorkOrderStatus newStatus;
        try {
            newStatus = WorkOrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid work order status: " + status);
            throw new IllegalArgumentException("Invalid work order status: " + status);
        }

        workOrder.setStatus(newStatus);
        workOrder.setUpdatedAt(OffsetDateTime.now());

        if (newStatus == WorkOrderStatus.COMPLETED || newStatus == WorkOrderStatus.CANCELLED) {
            Vehicle vehicle = workOrder.getVehicle();
            vehicle.setBookedForService(false);
            vehicle.setServiceDone(newStatus == WorkOrderStatus.COMPLETED);
            vehicle.setUpdatedAt(OffsetDateTime.now());
            vehicleRepo.save(vehicle);
            logger.info("Updated vehicle status for Vehicle ID: " + vehicle.getVehicleId() + " - Booked: " + vehicle.getBookedForService() + ", Service Done: " + vehicle.getServiceDone());
        }

        WorkOrder savedWorkOrder = workOrderRepo.save(workOrder);
        logger.info("Work Order ID: " + workOrderId + " updated to status: " + newStatus);
        return savedWorkOrder;
    }

    @Override
    @Transactional
    public void unassignVehicle(Long serviceManagerId, Long vehicleId)
            throws ServiceManagerDoesNotExistException, VehicleDoesNotExistException {
        logger.info("Unassigning vehicle ID: " + vehicleId + " from Service Manager ID: " + serviceManagerId);
        ServiceManager manager = serviceManagerRepo.findServiceManagerByUserId(serviceManagerId);
        if (manager == null) {
            logger.warning("Service Manager with ID " + serviceManagerId + " does not exist");
            throw new ServiceManagerDoesNotExistException("Service Manager with ID " + serviceManagerId + " does not exist");
        }

        Vehicle vehicle = vehicleRepo.findVehicleByVehicleId(vehicleId);
        if (vehicle == null) {
            logger.warning("Vehicle with ID " + vehicleId + " does not exist");
            throw new VehicleDoesNotExistException("Vehicle with ID " + vehicleId + " does not exist");
        }

        if (vehicle.getAssignedManager() == null || !vehicle.getAssignedManager().getUserId().equals(serviceManagerId)) {
            logger.warning("Vehicle ID: " + vehicleId + " is not assigned to Service Manager ID: " + serviceManagerId);
            throw new IllegalStateException("Vehicle is not assigned to this Service Manager");
        }

        manager.unassignVehicle(vehicle);
        vehicleRepo.save(vehicle);
        logger.info("Vehicle ID: " + vehicleId + " unassigned from Service Manager ID: " + serviceManagerId);
    }
}