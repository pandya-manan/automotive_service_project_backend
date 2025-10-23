package com.automotive.mechanic.service;

import com.automotive.mechanic.entity.*;
import com.automotive.mechanic.dto.MechanicWorkOrderResponse;
import com.automotive.mechanic.exception.WorkOrderException;
import com.automotive.mechanic.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MechanicServiceImpl implements com.automotive.mechanic.service.MechanicService {

    private final WorkOrderRepository workOrderRepo;

    public MechanicServiceImpl(WorkOrderRepository workOrderRepo) {
        this.workOrderRepo = workOrderRepo;
    }

    @Override
    public List<MechanicWorkOrderResponse> listAssignedWorkOrders(Long mechanicId) {
        return workOrderRepo.findByMechanic_UserId(mechanicId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MechanicWorkOrderResponse getWorkOrder(String serviceOrderId, Long mechanicId) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderIdAndMechanic_UserId(serviceOrderId, mechanicId)
                .orElseThrow(() -> new WorkOrderException("Work order not found or not assigned to this mechanic"));
        return mapToDto(wo);
    }

    @Override
    @Transactional
    public MechanicWorkOrderResponse startWorkOrder(String serviceOrderId, Long mechanicId) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderIdAndMechanic_UserId(serviceOrderId, mechanicId)
                .orElseThrow(() -> new WorkOrderException("Work order not found or not assigned to this mechanic"));
        if (wo.getStatus() != WorkOrderStatus.ASSIGNED) {
            throw new WorkOrderException("Only ASSIGNED work orders can be started");
        }
        wo.setStatus(WorkOrderStatus.IN_PROGRESS);
        wo.setStartedAt(OffsetDateTime.now());
        return mapToDto(wo);
    }

    @Override
    @Transactional
    public MechanicWorkOrderResponse completeWorkOrder(String serviceOrderId, Long mechanicId, Double finalCost) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderIdAndMechanic_UserId(serviceOrderId, mechanicId)
                .orElseThrow(() -> new WorkOrderException("Work order not found or not assigned to this mechanic"));
        if (wo.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new WorkOrderException("Only IN_PROGRESS work orders can be completed");
        }
        wo.setStatus(WorkOrderStatus.COMPLETED);
        wo.setCompletedAt(OffsetDateTime.now());
        wo.setFinalCost(finalCost);
        wo.getVehicle().setServiceDone(true);
        wo.getVehicle().setBookedForService(false);
        return mapToDto(wo);
    }

    @Override
    @Transactional
    public MechanicWorkOrderResponse updateProgress(String serviceOrderId, Long mechanicId, String progress) throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderIdAndMechanic_UserId(serviceOrderId, mechanicId)
                .orElseThrow(() -> new WorkOrderException("Work order not found or not assigned to this mechanic"));
        // append progress to description
        wo.setDescription((wo.getDescription() != null ? wo.getDescription() + "\n" : "") + progress);
        return mapToDto(wo);
    }

    private MechanicWorkOrderResponse mapToDto(WorkOrder wo) {
        MechanicWorkOrderResponse dto = new MechanicWorkOrderResponse();
        dto.setServiceOrderId(wo.getServiceOrderId());
        dto.setVehicleId(wo.getVehicle().getVehicleId());
        dto.setStatus(wo.getStatus());
        dto.setDescription(wo.getDescription());
        dto.setScheduledAt(wo.getScheduledAt());
        dto.setStartedAt(wo.getStartedAt());
        dto.setCompletedAt(wo.getCompletedAt());
        dto.setEstimatedCost(wo.getEstimatedCost());
        dto.setFinalCost(wo.getFinalCost());
        dto.setManagerId(wo.getAssignedBy() != null ? wo.getAssignedBy().getUserId() : null);
        return dto;
    }
}
