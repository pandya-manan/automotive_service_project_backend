package com.automotive.login.entity;

public class LoginResponseDTO {
    
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String userName;
    private String userEmail;
    private Role role;
    private String userType;
    
    public LoginResponseDTO() {
    }
    
    public LoginResponseDTO(String token, User user) {
        this.token = token;
        this.tokenType = "Bearer";
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.role = user.getRole();
        this.userType = user.getType();
    }
    
    public LoginResponseDTO(String token, String tokenType, Long userId, String userName, 
                           String userEmail, Role role, String userType) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
        this.userType = userType;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
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
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "token='" + token + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", role=" + role +
                ", userType='" + userType + '\'' +
                '}';
    }
}


