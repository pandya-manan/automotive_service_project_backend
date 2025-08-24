package com.automotive.login.dto;

import com.automotive.signup.entity.Role;

public class JwtResponseDTO {
    private String token;
    private String userEmail;
    private Role role;
    private String dashboardRedirect;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDashboardRedirect() {
        return dashboardRedirect;
    }

    public void setDashboardRedirect(String dashboardRedirect) {
        this.dashboardRedirect = dashboardRedirect;
    }

    @Override
    public String toString() {
        return "JwtResponseDTO{" +
                "token='[PROTECTED]'," +
                "userEmail='" + userEmail + '\'' +
                ", role=" + role +
                ", dashboardRedirect='" + dashboardRedirect + '\'' +
                '}';
    }
}
