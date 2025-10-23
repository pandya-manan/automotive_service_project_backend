package com.automotive.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class AssignMechanicRequest {
    @NotNull
    public Long mechanicId;

    @PositiveOrZero
    public Double estimatedCost;

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

	public AssignMechanicRequest(@NotNull Long mechanicId, @PositiveOrZero Double estimatedCost) {
		super();
		this.mechanicId = mechanicId;
		this.estimatedCost = estimatedCost;
	}

	public AssignMechanicRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
