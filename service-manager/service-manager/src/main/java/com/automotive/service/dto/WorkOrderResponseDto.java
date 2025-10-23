package com.automotive.service.dto;


import java.time.OffsetDateTime;

import com.automotive.service.entity.WorkOrderStatus;

public class WorkOrderResponseDto {
    public Long id;
    public String serviceOrderId;
    public Long vehicleId;
    public Long customerId;
    public WorkOrderStatus status;
    public Long assignedManagerId;
    public Long mechanicId;
    public Double estimatedCost;
    public Double finalCost;
    public OffsetDateTime scheduledAt;
    public String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceOrderId() {
		return serviceOrderId;
	}
	public void setServiceOrderId(String serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public WorkOrderStatus getStatus() {
		return status;
	}
	public void setStatus(WorkOrderStatus status) {
		this.status = status;
	}
	public Long getAssignedManagerId() {
		return assignedManagerId;
	}
	public void setAssignedManagerId(Long assignedManagerId) {
		this.assignedManagerId = assignedManagerId;
	}
	public Long getMechanicId() {
		return mechanicId;
	}
	public void setMechanicId(Long mechanicId) {
		this.mechanicId = mechanicId;
	}
	public Double getEstimatedCost() {
		return estimatedCost;
	}
	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}
	public Double getFinalCost() {
		return finalCost;
	}
	public void setFinalCost(Double finalCost) {
		this.finalCost = finalCost;
	}
	public OffsetDateTime getScheduledAt() {
		return scheduledAt;
	}
	public void setScheduledAt(OffsetDateTime scheduledAt) {
		this.scheduledAt = scheduledAt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
