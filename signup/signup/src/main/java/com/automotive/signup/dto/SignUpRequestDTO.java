package com.automotive.signup.dto;

import jakarta.validation.constraints.*;

public class SignUpRequestDTO {

    @NotNull(message="User Name cannot be null")
    @NotEmpty(message="User Name cannot be empty")
    @Size(min=3,max=20,message="The name must be between {min} and {max}")
    private String userName;

    @Email(message="Please provide a valid email address")
    @NotBlank(message="User Email cannot be empty")
    private String userEmail;

    @NotEmpty(message="User Password cannot be empty")
    @NotBlank(message="User Password cannot be blank")
    @NotNull(message="User Password cannot be null")
    @Size(min=8,max=12,message="User Password must be between {min} and {max}")
    private String userPassword;

    @NotNull(message="User Phone Number cannot be null")
    @Pattern(regexp = "^\\d{10}$", message="Phone number must be of 10 digit length")
    private String userPhoneNumber;

    @NotNull(message="User Address cannot be empty")
    @Size(min=50,max=500,message = "User Address must be between {min} and {max}")
    private String userAddress;

    @NotNull(message="Role must not be null")
    @NotBlank(message="Role must not be blank")
    @NotEmpty(message="Role must not be empty")
    private String role;

    @Min(value = 1, message = "Years of experience must be at least {value}.")
    @Max(value = 25, message = "Years of experience must not exceed {value}.")
    private Integer yearsOfExperience;
    
    @Size(min=5,max=20,message="Service Department needs to be within {min} and {max} characters")
    private String serviceDepartment;
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getServiceDepartment() {
        return serviceDepartment;
    }

    public void setServiceDepartment(String serviceDepartment) {
        this.serviceDepartment = serviceDepartment;
    }

    public SignUpRequestDTO(String userName, String userEmail, String userPassword, String userPhoneNumber, String userAddress, String role, Integer yearsOfExperience, String serviceDepartment) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userAddress = userAddress;
        this.role = role;
        this.yearsOfExperience = yearsOfExperience;
        this.serviceDepartment = serviceDepartment;
    }

    public SignUpRequestDTO() {
    }

    @Override
    public String toString() {
        return "SignUpRequestDTO{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", role='" + role + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", serviceDepartment='" + serviceDepartment + '\'' +
                '}';
    }
}
