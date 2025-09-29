package com.automotive.customer.entity;

import java.time.OffsetDateTime;

public class ServiceStatusDTO {
	
	private String registrationNumber;
    private Boolean bookingStatus;
    private Boolean serviceCompleted;
    private OffsetDateTime serviceBookingDate;
    private String serviceStatus;
    private Long serviceManagerId;
    private Long mechanicId;
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public Boolean getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(Boolean bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public Boolean getServiceCompleted() {
		return serviceCompleted;
	}
	public void setServiceCompleted(Boolean serviceCompleted) {
		this.serviceCompleted = serviceCompleted;
	}
	public OffsetDateTime getServiceBookingDate() {
		return serviceBookingDate;
	}
	public void setServiceBookingDate(OffsetDateTime serviceBookingDate) {
		this.serviceBookingDate = serviceBookingDate;
	}
	public String getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public Long getServiceManagerId() {
		return serviceManagerId;
	}
	public void setServiceManagerId(Long serviceManagerId) {
		this.serviceManagerId = serviceManagerId;
	}
	public Long getMechanicId() {
		return mechanicId;
	}
	public void setMechanicId(Long mechanicId) {
		this.mechanicId = mechanicId;
	}
    
    

}
