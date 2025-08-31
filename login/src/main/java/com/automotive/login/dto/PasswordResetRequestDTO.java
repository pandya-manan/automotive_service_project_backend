package com.automotive.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PasswordResetRequestDTO {
    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "User Email cannot be empty")
    @NotEmpty(message = "User Email cannot be empty")
    @NotNull(message = "User Email cannot be null")
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "PasswordResetRequestDTO{" +
                "userEmail='" + userEmail + '\'' +
                '}';
    }
}
