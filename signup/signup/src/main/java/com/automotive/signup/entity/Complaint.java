package com.automotive.signup.entity;
import jakarta.persistence.*;
import java.time.*;
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    private Long id; // same as CustomerFeedback.id if synced

    private Long customerId;
    private String serviceOrderId;
    private String subject;
    private String message;
    private FeedbackStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private Long assignedAgentId;
    private String agentResponse;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getServiceOrderId() {
		return serviceOrderId;
	}
	public void setServiceOrderId(String serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public FeedbackStatus getStatus() {
		return status;
	}
	public void setStatus(FeedbackStatus status) {
		this.status = status;
	}
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Long getAssignedAgentId() {
		return assignedAgentId;
	}
	public void setAssignedAgentId(Long assignedAgentId) {
		this.assignedAgentId = assignedAgentId;
	}
	public String getAgentResponse() {
		return agentResponse;
	}
	public void setAgentResponse(String agentResponse) {
		this.agentResponse = agentResponse;
	}
    
    
}
