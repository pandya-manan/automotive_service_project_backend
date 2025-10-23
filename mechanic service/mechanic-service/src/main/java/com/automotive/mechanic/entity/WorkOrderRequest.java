package com.automotive.mechanic.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class WorkOrderRequest {

    @NotNull(message = "Description cannot be null")
    @NotEmpty(message="Description cannot be empty")
    @Size(min=50,max=2000,message="Description length is between 50 and 2000 characters")
    private String description;

    @NotNull(message="Scheduled At cannot be null")
    @NotEmpty(message="Scheduled At cannot be empty")
    private OffsetDateTime scheduledAt;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(OffsetDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

}
