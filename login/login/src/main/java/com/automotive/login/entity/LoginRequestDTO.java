package com.automotive.login.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
	
	@Email(message="Please enter a valid email")
	@NotNull(message="User email cannot be null")
	@NotEmpty(message="User email cannot be empty")
	private String userEmail;
	
	@Size(min=8,max=12,message="The password length is from 8 to 12 characters")
	@NotNull(message="User Password cannot be null")
	@NotEmpty(message="User Password cannot be empty")
	private String userPassword;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public LoginRequestDTO(String userEmail, String userPassword) {
		super();
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	@Override
	public String toString() {
		return "LoginRequestDTO [userEmail=" + userEmail + ", userPassword=" + userPassword + "]";
	}
	
	

}
