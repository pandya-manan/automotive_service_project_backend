package com.automotive.service.serviceimpl;

import com.automotive.service.dto.*;
import com.automotive.service.dto.WorkOrderResponseDto;
import com.automotive.service.entity.*;
import com.automotive.service.exception.WorkOrderException;
import com.automotive.service.repository.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class ServiceManagerService {

    private final WorkOrderRepository workOrderRepo;
    private final VehicleRepository vehicleRepo;
    private final ServiceManagerRepository managerRepo;
    private final MechanicRepository mechanicRepo;
    private final RestClient restClient;
    
    @Value("${email-url}")
    private String EMAIL_SERVICE_URL;
    
    private static final Logger logger = Logger.getLogger(ServiceManagerService.class.getName());

    public ServiceManagerService(WorkOrderRepository workOrderRepo,
                                 VehicleRepository vehicleRepo,
                                 ServiceManagerRepository managerRepo,
                                 MechanicRepository mechanicRepo,RestClient restClient) {
        this.workOrderRepo = workOrderRepo;
        this.vehicleRepo = vehicleRepo;
        this.managerRepo = managerRepo;
        this.mechanicRepo = mechanicRepo;
        this.restClient=restClient;
    }

    public List<WorkOrderResponseDto> listOpenWorkOrders() {
//        List<WorkOrder> list = workOrderRepo.findByStatus(WorkOrderStatus.OPEN);
    	List<WorkOrder> list=workOrderRepo.findAll();
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public WorkOrderResponseDto getByServiceOrderId(String serviceOrderId) throws WorkOrderException {
        return workOrderRepo.findByServiceOrderId(serviceOrderId).map(this::toDto).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
    }

    @Transactional
    public WorkOrderResponseDto assignManager(String serviceOrderId, AssignManagerRequest req) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
        ServiceManager manager = managerRepo.findById(req.managerId).orElseThrow(() -> new WorkOrderException("ServiceManager not found"));

        wo.setAssignedBy(manager);
        wo.setStatus(WorkOrderStatus.ASSIGNED);
        wo.setUpdatedAt(OffsetDateTime.now());
        workOrderRepo.save(wo);

        // set vehicle.assignedManager
        Vehicle vehicle = wo.getVehicle();
        if (vehicle != null) {
            vehicle.setAssignedManager(manager);
            vehicle.setBookedForService(Boolean.TRUE); // ensure flag remains true
            vehicle.setServiceDone(Boolean.FALSE);
            vehicle.setUpdatedAt(OffsetDateTime.now());
            vehicleRepo.save(vehicle);
        }

        return toDto(wo);
    }

    @Transactional
    public WorkOrderResponseDto assignMechanic(String serviceOrderId, AssignMechanicRequest req) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
        Mechanic mech = mechanicRepo.findById(req.mechanicId).orElseThrow(() -> new WorkOrderException("Mechanic not found"));

        wo.setMechanic(mech);
        wo.setEstimatedCost(req.estimatedCost);
        wo.setStatus(WorkOrderStatus.ASSIGNED);
        wo.setUpdatedAt(OffsetDateTime.now());
        workOrderRepo.save(wo);

        Vehicle vehicle = wo.getVehicle();
        if (vehicle != null) {
            vehicle.setAssignedManager(wo.getAssignedBy()); // assigned manager remains
            vehicle.setBookedForService(Boolean.TRUE);
            vehicle.setServiceDone(Boolean.FALSE);
            vehicle.setUpdatedAt(OffsetDateTime.now());
            vehicleRepo.save(vehicle);
        }

        return toDto(wo);
    }

    @Transactional
    public WorkOrderResponseDto startWorkOrder(String serviceOrderId, SimpleIdRequest req) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
        wo.setStartedAt(OffsetDateTime.now());
        wo.setStatus(WorkOrderStatus.IN_PROGRESS);
        wo.setUpdatedAt(OffsetDateTime.now());
        workOrderRepo.save(wo);
        return toDto(wo);
    }

//    @Transactional
//    public WorkOrderResponseDto completeWorkOrder(String serviceOrderId, UpdateCostsRequest req) throws WorkOrderException {
//        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
//
//        if (req.finalCost != null) {
//            wo.setFinalCost(req.finalCost);
//        }
//        if (req.estimatedCost != null) {
//            wo.setEstimatedCost(req.estimatedCost);
//        }
//        wo.setCompletedAt(OffsetDateTime.now());
//        wo.setStatus(WorkOrderStatus.COMPLETED);
//        wo.setUpdatedAt(OffsetDateTime.now());
//        workOrderRepo.save(wo);
//
//        // update vehicle flags
//        Vehicle vehicle = wo.getVehicle();
//        if (vehicle != null) {
//            vehicle.setServiceDone(Boolean.TRUE);
//            vehicle.setBookedForService(Boolean.FALSE);
//            vehicle.setUpdatedAt(OffsetDateTime.now());
//            // optionally clear assigned manager if desired
//            // vehicle.setAssignedManager(null);
//            vehicleRepo.save(vehicle);
//        }
//
//        return toDto(wo);
//    }
    
    @Transactional
    public WorkOrderResponseDto completeWorkOrder(String serviceOrderId, UpdateCostsRequest req) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));

        if (req.estimatedCost != null) {
            wo.setEstimatedCost(req.estimatedCost);
        }
        if (req.finalCost != null) {
            wo.setFinalCost(req.finalCost);
        }

        wo.setCompletedAt(OffsetDateTime.now());
        wo.setStatus(WorkOrderStatus.COMPLETED);
        wo.setUpdatedAt(OffsetDateTime.now());
        workOrderRepo.save(wo);

        // update vehicle flags
        Vehicle vehicle = wo.getVehicle();
        if (vehicle != null) {
            vehicle.setServiceDone(Boolean.TRUE);
            vehicle.setBookedForService(Boolean.FALSE);
            vehicle.setUpdatedAt(OffsetDateTime.now());
            // optionally clear assigned manager if desired
            vehicle.setAssignedManager(null);
            // vehicle.setAssignedManager(null);
            vehicleRepo.save(vehicle);
        }

        // -------------------------------
        // Trigger completion email (uses the existing DTO in email service)
        // -------------------------------
        try {
            if (vehicle != null && vehicle.getOwner() != null) {
                ServiceCompletionEmailRequestDTO emailDto = new ServiceCompletionEmailRequestDTO();

                // required fields used by your email service
                emailDto.setTo(vehicle.getOwner().getUserEmail());
                emailDto.setFrom("pitstopprowssoa@gmail.com"); // or read from config if you prefer
                emailDto.setServiceOrderId(wo.getServiceOrderId());
                emailDto.setVehicleVin(vehicle.getVin());
                emailDto.setMake(vehicle.getMake());
                emailDto.setModel(vehicle.getModel());
                emailDto.setUserName(vehicle.getOwner().getUserName());
                emailDto.setCompletedAt(wo.getCompletedAt());

                // optional: service manager and mechanic names if present
                emailDto.setServiceManager(wo.getAssignedBy() != null ? wo.getAssignedBy().getUserName() : null);
                emailDto.setMechanic(wo.getMechanic() != null ? wo.getMechanic().getUserName() : null);

                // costs (use finalCost if present else estimated)
                double estimated = wo.getEstimatedCost() != null ? wo.getEstimatedCost() : 0.0;
                double finalCost = wo.getFinalCost() != null ? wo.getFinalCost() : estimated;
                emailDto.setEstimatedCost(estimated);
                emailDto.setFinalCost(finalCost);

                // call email service (same pattern as your signup code)
                restClient.post()
                        .uri(EMAIL_SERVICE_URL)
                        .body(emailDto)
                        .retrieve()
                        .toEntity(String.class);

                logger.info("Service completion email triggered for serviceOrderId=" + wo.getServiceOrderId()
                        + " to " + vehicle.getOwner().getUserEmail());
            }
        } catch (Exception e) {
            // log and proceed — email failure shouldn't break the completion flow
            logger.warning("Failed to send completion email for " + wo.getServiceOrderId() + ": " + e.getMessage());
        }

        return toDto(wo);
    }

    @Transactional
    public WorkOrderResponseDto updateCosts(String serviceOrderId, UpdateCostsRequest req) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderId(serviceOrderId).orElseThrow(() -> new WorkOrderException("WorkOrder not found"));
        if (req.estimatedCost != null) wo.setEstimatedCost(req.estimatedCost);
        if (req.finalCost != null) wo.setFinalCost(req.finalCost);
        wo.setUpdatedAt(OffsetDateTime.now());
        workOrderRepo.save(wo);
        return toDto(wo);
    }

    private WorkOrderResponseDto toDto(WorkOrder wo) {
        WorkOrderResponseDto d = new WorkOrderResponseDto();
        d.id = wo.getId();
        d.serviceOrderId = wo.getServiceOrderId();
        d.vehicleId = wo.getVehicle() != null ? wo.getVehicle().getVehicleId() : null;
        d.customerId = wo.getVehicle() != null && wo.getVehicle().getOwner() != null ? wo.getVehicle().getOwner().getUserId() : null;
        d.status = wo.getStatus();
        d.assignedManagerId = wo.getAssignedBy() != null ? wo.getAssignedBy().getUserId() : null;
        d.mechanicId = wo.getMechanic() != null ? wo.getMechanic().getUserId() : null;
        d.estimatedCost = wo.getEstimatedCost();
        d.finalCost = wo.getFinalCost();
        d.scheduledAt = wo.getScheduledAt();
        d.description = wo.getDescription();
        return d;
    }
}
