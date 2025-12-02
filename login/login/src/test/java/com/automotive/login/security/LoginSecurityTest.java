package com.automotive.login.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.automotive.login.entity.Customer;
import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.Role;
import com.automotive.login.entity.User;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;
import com.automotive.login.repository.UserRepository;
import com.automotive.login.service.LoginServiceImpl;
import com.automotive.login.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("Login Security Tests")
class LoginSecurityTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl loginService;

    private User testUser;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new Customer();
        testUser.setUserId(1L);
        testUser.setUserEmail("test@example.com");
        testUser.setUserPassword("$2a$10$encryptedPasswordHash");
        testUser.setRole(Role.CUSTOMER);

        loginRequest = new LoginRequestDTO("test@example.com", "password123");
    }

    @Test
    @DisplayName("Should generate JWT token on successful login")
    void testLogin_GeneratesJwtToken() throws Exception {
        // Arrange
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getUserPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(testUser)).thenReturn("valid-jwt-token");

        // Act
        var result = loginService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("valid-jwt-token", result.getToken());
        verify(jwtTokenUtil, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Should not generate token when user not found")
    void testLogin_NoTokenWhenUserNotFound() {
        // Arrange
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, 
            () -> loginService.login(loginRequest));
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should not generate token when password is incorrect")
    void testLogin_NoTokenWhenPasswordIncorrect() {
        // Arrange
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getUserPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, 
            () -> loginService.login(loginRequest));
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should validate email is provided")
    void testLogin_EmailRequired() {
        // Arrange
        LoginRequestDTO requestWithoutEmail = new LoginRequestDTO(null, "password123");

        // Act & Assert
        assertThrows(UserNotFoundException.class, 
            () -> loginService.login(requestWithoutEmail));
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should validate password is provided")
    void testLogin_PasswordRequired() {
        // Arrange
        LoginRequestDTO requestWithoutPassword = new LoginRequestDTO("test@example.com", null);
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches(null, testUser.getUserPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, 
            () -> loginService.login(requestWithoutPassword));
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should return user details with token")
    void testLogin_ReturnsUserDetailsWithToken() throws Exception {
        // Arrange
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getUserPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(testUser)).thenReturn("valid-jwt-token");

        // Act
        var result = loginService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUserEmail());
        assertEquals(testUser.getUserEmail(), result.getUserEmail());
    }
}
