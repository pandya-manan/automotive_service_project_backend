package com.automotive.service.dto;

import jakarta.validation.constraints.NotNull;

public class SimpleIdRequest {
    @NotNull
    public Long userId; // manager or mechanic performing action

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public SimpleIdRequest(@NotNull Long userId) {
		super();
		this.userId = userId;
	}

	public SimpleIdRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
