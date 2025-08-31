package com.automotive.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirmRequestDTO {
    @NotBlank(message = "Token cannot be empty")
    @NotEmpty(message = "Token cannot be empty")
    @NotNull(message = "Token cannot be null")
    private String token;

    @NotEmpty(message = "New Password cannot be empty")
    @NotBlank(message = "New Password cannot be blank")
    @NotNull(message = "New Password cannot be null")
    @Size(min = 8, max = 12, message = "New Password must be between {min} and {max}")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordResetConfirmRequestDTO{" +
                "token='[PROTECTED]'," +
                "newPassword='[PROTECTED]'" +
                '}';
    }
}
