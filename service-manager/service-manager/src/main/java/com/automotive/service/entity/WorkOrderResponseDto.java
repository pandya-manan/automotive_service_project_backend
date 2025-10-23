package com.automotive.service.entity;

import java.time.OffsetDateTime;

public class WorkOrderResponseDto {

    private Long workOrderId;

    private Long vehicleId;

    private WorkOrderStatus status;

    private OffsetDateTime scheduledAt;

    private String description;

    private Double estimatedCost;

    public WorkOrderResponseDto(Long workOrderId,Long vehicleId,WorkOrderStatus status,OffsetDateTime scheduledAt,String description)
    {
        this.workOrderId=workOrderId;
        this.vehicleId=vehicleId;
        this.status=status;
        this.scheduledAt=scheduledAt;
        this.description=description;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
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

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
}
