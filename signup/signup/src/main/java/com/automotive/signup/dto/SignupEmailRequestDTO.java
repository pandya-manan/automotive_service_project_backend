package com.automotive.signup.dto;

public class SignupEmailRequestDTO {

    private String to;

    private String name;

    private String role;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "SignupEmailRequestDTO{" +
                "to='" + to + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public SignupEmailRequestDTO(String to, String name, String role) {
        this.to = to;
        this.role = role;
        this.name = name;
    }
}
