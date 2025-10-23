package com.automotive.service.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class UpdateCostsRequest {
    @PositiveOrZero
    public Double estimatedCost;

    @PositiveOrZero
    public Double finalCost;

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

	public UpdateCostsRequest(@PositiveOrZero Double estimatedCost, @PositiveOrZero Double finalCost) {
		super();
		this.estimatedCost = estimatedCost;
		this.finalCost = finalCost;
	}

	public UpdateCostsRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
