package com.automotive.mechanic.dto;


public class WorkOrderCompletionRequest {
    private Double finalCost;
    private String serviceImageUrl;
    private String serviceDetails;
	public Double getFinalCost() {
		return finalCost;
	}
	public void setFinalCost(Double finalCost) {
		this.finalCost = finalCost;
	}
	public String getServiceImageUrl() {
		return serviceImageUrl;
	}
	public void setServiceImageUrl(String serviceImageUrl) {
		this.serviceImageUrl = serviceImageUrl;
	}
	public String getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
    
    
}