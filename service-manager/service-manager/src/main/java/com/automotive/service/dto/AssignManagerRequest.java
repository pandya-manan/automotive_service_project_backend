package com.automotive.service.dto;

import jakarta.validation.constraints.NotNull;

public class AssignManagerRequest {
    @NotNull
    public Long managerId;

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public AssignManagerRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AssignManagerRequest(@NotNull Long managerId) {
		super();
		this.managerId = managerId;
	}
    
    
}
