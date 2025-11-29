package com.automotive.mechanic.dto;

import com.automotive.mechanic.entity.WorkOrderStatus;

import java.time.OffsetDateTime;

public class MechanicWorkOrderResponse {

    private String serviceOrderId;
    private Long vehicleId;
    private WorkOrderStatus status;
    private String description;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private Double estimatedCost;
    private Double finalCost;
    private Long managerId;
    private String vehicleImageUrl;

    public String getVehicleImageUrl() {
		return vehicleImageUrl;
	}
	public void setVehicleImageUrl(String vehicleImageUrl) {
		this.vehicleImageUrl = vehicleImageUrl;
	}
	// Getters & Setters
    public String getServiceOrderId() { return serviceOrderId; }
    public void setServiceOrderId(String serviceOrderId) { this.serviceOrderId = serviceOrderId; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public OffsetDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(OffsetDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public OffsetDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(OffsetDateTime startedAt) { this.startedAt = startedAt; }

    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }

    public Double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(Double estimatedCost) { this.estimatedCost = estimatedCost; }

    public Double getFinalCost() { return finalCost; }
    public void setFinalCost(Double finalCost) { this.finalCost = finalCost; }

    public Long getManagerId() { return managerId; }
    public void setManagerId(Long managerId) { this.managerId = managerId; }
}
