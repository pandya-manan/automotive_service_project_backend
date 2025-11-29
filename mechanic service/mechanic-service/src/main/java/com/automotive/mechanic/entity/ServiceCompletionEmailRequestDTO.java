package com.automotive.mechanic.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServiceCompletionEmailRequestDTO {
    
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;
    
    @NotBlank(message="From email is required")
    @Email(message="Invalid email format")
    private String from;
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotBlank(message = "Service type is required")
    private String serviceType;
    
    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;
    
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Service date is required")
    private String serviceDate;
    
    @NotBlank(message = "Completion date is required")
    private String completionDate;
    
    @NotBlank(message = "Mechanic name is required")
    private String mechanicName;
    
    @NotBlank(message = "Service center info is required")
    private String serviceCenterInfo;
    
    @NotNull(message = "Final cost is required")
    private Double finalCost;
    
    private String serviceImageUrl; // New field for the captured image
    private String invoiceNumber; // New field for invoice
    private String serviceDetails; // New field for detailed service description
    
    
    
    
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	public String getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(String completionDate) {
		this.completionDate = completionDate;
	}
	public String getMechanicName() {
		return mechanicName;
	}
	public void setMechanicName(String mechanicName) {
		this.mechanicName = mechanicName;
	}
	public String getServiceCenterInfo() {
		return serviceCenterInfo;
	}
	public void setServiceCenterInfo(String serviceCenterInfo) {
		this.serviceCenterInfo = serviceCenterInfo;
	}
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
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(String serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
    
    
    
}