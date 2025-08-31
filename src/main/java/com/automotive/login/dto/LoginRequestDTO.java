package com.automotive.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "User Email cannot be empty")
    @NotEmpty(message = "User Email cannot be empty")
    @NotNull(message = "User Email cannot be null")
    private String userEmail;

    @NotEmpty(message = "User Password cannot be empty")
    @NotBlank(message = "User Password cannot be blank")
    @NotNull(message = "User Password cannot be null")
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

    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassword='[PROTECTED]'" +
                '}';
    }
}