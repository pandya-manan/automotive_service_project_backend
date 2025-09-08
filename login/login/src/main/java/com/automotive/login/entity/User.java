package com.automotive.login.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="user_name",nullable = false)
    private String userName;

    @Column(name="user_email",unique = true,nullable = false)
    private String userEmail;

    @Column(name="user_password",nullable = false)
    private String userPassword;

    @Column(name="user_phone_number",nullable = false,unique = true)
    private String userPhoneNumber;

    @Column(name="user_address",length = 500,nullable = false)
    private String userAddress;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public Role getRole() {
        return role;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", role=" + role +
                '}';
    }

    public User(Long userId, String userName, String userEmail, String userPassword, String userPhoneNumber, String userAddress, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userAddress = userAddress;
        this.role = role;
    }

    public User()
    {

    }
}
