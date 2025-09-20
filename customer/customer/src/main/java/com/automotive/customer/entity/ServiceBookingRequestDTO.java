package com.automotive.customer.entity;

import java.time.OffsetDateTime;

public class ServiceBookingRequestDTO {
	
	String serviceOrderId;
	
	private OffsetDateTime createdAt;
	
	private String vehicleVin;
	
	private String make;
	
	private String model;
	
	private String to;
	
	private String from;
	
	private String userName;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceOrderId() {
		return serviceOrderId;
	}

	public void setServiceOrderId(String serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getVehicleVin() {
		return vehicleVin;
	}

	public void setVehicleVin(String vehicleVin) {
		this.vehicleVin = vehicleVin;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "ServiceBookingRequestDTO [serviceOrderId=" + serviceOrderId + ", createdAt=" + createdAt
				+ ", vehicleVin=" + vehicleVin + ", make=" + make + ", model=" + model + ", to=" + to + ", from=" + from
				+ "]";
	}
	
	

}
