package com.automotive.mechanic.service;

import com.automotive.mechanic.entity.*;
import com.automotive.mechanic.dto.MechanicWorkOrderResponse;
import com.automotive.mechanic.dto.WorkOrderCompletionRequest;
import com.automotive.mechanic.exception.WorkOrderException;
import com.automotive.mechanic.repository.WorkOrderRepository;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;



@Service
public class MechanicServiceImpl implements com.automotive.mechanic.service.MechanicService {

	private final RestClient restClient;
	
	@Value("${email-url}")
	private String EMAIL_SERVICE_URL;
	
    private final WorkOrderRepository workOrderRepo;
    
    private static final Logger logger = Logger.getLogger(MechanicServiceImpl.class.getName());

    public MechanicServiceImpl(WorkOrderRepository workOrderRepo,RestClient restClient) {
        this.workOrderRepo = workOrderRepo;
        this.restClient=restClient;
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
    public MechanicWorkOrderResponse completeWorkOrder(String serviceOrderId, Long mechanicId, 
                                                      WorkOrderCompletionRequest completionRequest) 
                                                      throws WorkOrderException {
        WorkOrder wo = workOrderRepo.findByServiceOrderIdAndMechanic_UserId(serviceOrderId, mechanicId)
                .orElseThrow(() -> new WorkOrderException("Work order not found or not assigned to this mechanic"));
        
        if (wo.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new WorkOrderException("Only IN_PROGRESS work orders can be completed");
        }
        
        wo.setStatus(WorkOrderStatus.COMPLETED);
        wo.setCompletedAt(OffsetDateTime.now());
        wo.setFinalCost(completionRequest.getFinalCost());
        wo.getVehicle().setServiceDone(true);
        wo.getVehicle().setBookedForService(false);
        
        // Store service details and image URL if provided
        if (completionRequest.getServiceDetails() != null) {
            wo.setDescription((wo.getDescription() != null ? wo.getDescription() + "\n" : "") + 
                             "Service Details: " + completionRequest.getServiceDetails());
        }
        
        // Send completion email with image
        sendServiceCompletionEmail(wo, completionRequest.getServiceImageUrl(), completionRequest.getServiceDetails());
        
        return mapToDto(wo);
    }

    private void sendServiceCompletionEmail(WorkOrder workOrder, String serviceImageUrl, String serviceDetails) {
        try {
            ServiceCompletionEmailRequestDTO emailDto = new ServiceCompletionEmailRequestDTO();
            
            // Populate email DTO from work order
            emailDto.setTo(workOrder.getVehicle().getOwner().getUserEmail());
            emailDto.setCustomerName(workOrder.getVehicle().getOwner().getUserName());
            emailDto.setServiceType(workOrder.getDescription());
            emailDto.setVehicleModel(workOrder.getVehicle().getModel());
            emailDto.setLicensePlate(workOrder.getVehicle().getRegistrationNumber());
            emailDto.setServiceDate(workOrder.getScheduledAt().format(DateTimeFormatter.ISO_LOCAL_DATE));
            emailDto.setCompletionDate(LocalDate.now().toString());
            emailDto.setMechanicName(workOrder.getMechanic().getUserName());
            emailDto.setServiceCenterInfo("Automotive Service Center\nPhone: ++91 93282 86456");
            emailDto.setFinalCost(workOrder.getFinalCost());
            emailDto.setServiceImageUrl(serviceImageUrl);
            emailDto.setServiceDetails(serviceDetails);
            emailDto.setFrom("pitstopprowssoa@gmail.com");
            
            // Call email service (you'll need to implement this REST call)
            // emailServiceClient.sendServiceCompletionEmail(emailDto);
            restClient.post()
            .uri(EMAIL_SERVICE_URL)
            .body(emailDto)
            .retrieve()
            .toEntity(String.class);

    logger.info("Service completion email triggered for serviceOrderId=" + emailDto.getLicensePlate()
            + " to " + emailDto.getCustomerName());
            
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to send completion email: " + e.getMessage());
        }
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
        dto.setVehicleImageUrl(wo.getVehicle().getVehicleImageUrl());
        return dto;
    }
}
