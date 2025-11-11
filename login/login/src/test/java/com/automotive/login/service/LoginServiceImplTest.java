package com.automotive.login.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.automotive.login.entity.Customer;
import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.LoginResponseDTO;
import com.automotive.login.entity.Role;
import com.automotive.login.entity.User;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;
import com.automotive.login.repository.UserRepository;
import com.automotive.login.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginServiceImpl Tests")
class LoginServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginServiceImpl loginService;

    private LoginRequestDTO validLoginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create a valid login request
        validLoginRequest = new LoginRequestDTO("test@example.com", "password123");

        // Create a test user (using Customer as concrete implementation)
        testUser = new Customer();
        testUser.setUserId(1L);
        testUser.setUserName("Test User");
        testUser.setUserEmail("test@example.com");
        testUser.setUserPassword("$2a$10$encryptedPasswordHash"); // Encrypted password stored in DB
        testUser.setUserPhoneNumber("1234567890");
        testUser.setUserAddress("123 Test Street");
        testUser.setRole(Role.CUSTOMER);
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void testLogin_Success() throws UserNotFoundException, InvalidPasswordException {
        // Arrange
        String mockToken = "mock.jwt.token";
        when(userRepository.findByUserEmail(validLoginRequest.getUserEmail()))
                .thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getUserPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(testUser)).thenReturn(mockToken);

        // Act
        LoginResponseDTO result = loginService.login(validLoginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockToken, result.getToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(testUser.getUserId(), result.getUserId());
        assertEquals(testUser.getUserEmail(), result.getUserEmail());
        assertEquals(testUser.getUserName(), result.getUserName());
        assertEquals(testUser.getRole(), result.getRole());
        assertEquals(testUser.getType(), result.getUserType());
        
        // Verify repository, password encoder, and JWT util were called
        verify(userRepository, times(1)).findByUserEmail(validLoginRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("password123", testUser.getUserPassword());
        verify(jwtTokenUtil, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void testLogin_UserNotFound() {
        // Arrange
        when(userRepository.findByUserEmail(anyString())).thenReturn(null);

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> loginService.login(validLoginRequest)
        );

        assertEquals("No user found with email: " + validLoginRequest.getUserEmail(), 
                     exception.getMessage());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByUserEmail(validLoginRequest.getUserEmail());
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when password is incorrect")
    void testLogin_InvalidPassword() {
        // Arrange
        LoginRequestDTO invalidPasswordRequest = new LoginRequestDTO(
                "test@example.com", 
                "wrongpassword"
        );
        when(userRepository.findByUserEmail(invalidPasswordRequest.getUserEmail()))
                .thenReturn(testUser);
        when(passwordEncoder.matches("wrongpassword", testUser.getUserPassword())).thenReturn(false);

        // Act & Assert
        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> loginService.login(invalidPasswordRequest)
        );

        assertEquals("Wrong password entered. Please try again.", exception.getMessage());
        
        // Verify repository and password encoder were called
        verify(userRepository, times(1)).findByUserEmail(invalidPasswordRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("wrongpassword", testUser.getUserPassword());
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when password is null")
    void testLogin_NullPassword() {
        // Arrange
        LoginRequestDTO nullPasswordRequest = new LoginRequestDTO("test@example.com", null);
        when(userRepository.findByUserEmail(nullPasswordRequest.getUserEmail()))
                .thenReturn(testUser);
        // BCrypt's matches() returns false for null passwords
        when(passwordEncoder.matches(null, testUser.getUserPassword())).thenReturn(false);

        // Act & Assert - Service throws InvalidPasswordException when password is null
        // because passwordEncoder.matches() returns false for null
        assertThrows(
                InvalidPasswordException.class,
                () -> loginService.login(nullPasswordRequest)
        );
        
        // Verify repository and password encoder were called
        verify(userRepository, times(1)).findByUserEmail(nullPasswordRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches(null, testUser.getUserPassword());
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when password is empty")
    void testLogin_EmptyPassword() {
        // Arrange
        LoginRequestDTO emptyPasswordRequest = new LoginRequestDTO("test@example.com", "");
        when(userRepository.findByUserEmail(emptyPasswordRequest.getUserEmail()))
                .thenReturn(testUser);
        when(passwordEncoder.matches("", testUser.getUserPassword())).thenReturn(false);

        // Act & Assert
        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> loginService.login(emptyPasswordRequest)
        );

        assertEquals("Wrong password entered. Please try again.", exception.getMessage());
        
        // Verify repository and password encoder were called
        verify(userRepository, times(1)).findByUserEmail(emptyPasswordRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("", testUser.getUserPassword());
    }

    @Test
    @DisplayName("Should handle case-sensitive password matching")
    void testLogin_CaseSensitivePassword() {
        // Arrange
        LoginRequestDTO caseSensitiveRequest = new LoginRequestDTO(
                "test@example.com", 
                "PASSWORD123"  // Different case
        );
        when(userRepository.findByUserEmail(caseSensitiveRequest.getUserEmail()))
                .thenReturn(testUser);
        when(passwordEncoder.matches("PASSWORD123", testUser.getUserPassword())).thenReturn(false);

        // Act & Assert
        InvalidPasswordException exception = assertThrows(
                InvalidPasswordException.class,
                () -> loginService.login(caseSensitiveRequest)
        );

        assertEquals("Wrong password entered. Please try again.", exception.getMessage());
        
        // Verify repository and password encoder were called
        verify(userRepository, times(1)).findByUserEmail(caseSensitiveRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("PASSWORD123", testUser.getUserPassword());
    }

    @Test
    @DisplayName("Should handle different user roles successfully")
    void testLogin_DifferentUserRoles() throws UserNotFoundException, InvalidPasswordException {
        // Arrange - Test with SERVICE_MANAGER role
        User serviceManager = new Customer();
        serviceManager.setUserId(2L);
        serviceManager.setUserName("Service Manager");
        serviceManager.setUserEmail("manager@example.com");
        serviceManager.setUserPassword("$2a$10$encryptedManagerPassword"); // Encrypted password
        serviceManager.setUserPhoneNumber("9876543210");
        serviceManager.setUserAddress("456 Manager Street");
        serviceManager.setRole(Role.SERVICE_MANAGER);

        LoginRequestDTO managerLoginRequest = new LoginRequestDTO(
                "manager@example.com", 
                "manager123"
        );
        String mockToken = "mock.jwt.token.manager";
        when(userRepository.findByUserEmail(managerLoginRequest.getUserEmail()))
                .thenReturn(serviceManager);
        when(passwordEncoder.matches("manager123", serviceManager.getUserPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(serviceManager)).thenReturn(mockToken);

        // Act
        LoginResponseDTO result = loginService.login(managerLoginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(mockToken, result.getToken());
        assertEquals(Role.SERVICE_MANAGER, result.getRole());
        assertEquals("manager@example.com", result.getUserEmail());
        
        // Verify repository, password encoder, and JWT util were called
        verify(userRepository, times(1)).findByUserEmail(managerLoginRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("manager123", serviceManager.getUserPassword());
        verify(jwtTokenUtil, times(1)).generateToken(serviceManager);
    }

    @Test
    @DisplayName("Should handle user with different email case")
    void testLogin_EmailCaseSensitivity() {
        // Arrange - User exists with lowercase email, but request has different case
        LoginRequestDTO differentCaseEmailRequest = new LoginRequestDTO(
                "TEST@EXAMPLE.COM",  // Different case
                "password123"
        );
        when(userRepository.findByUserEmail(differentCaseEmailRequest.getUserEmail()))
                .thenReturn(null);  // Repository won't find it due to case difference

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> loginService.login(differentCaseEmailRequest)
        );

        assertEquals("No user found with email: " + differentCaseEmailRequest.getUserEmail(), 
                     exception.getMessage());
        
        // Verify repository was called
        verify(userRepository, times(1)).findByUserEmail(differentCaseEmailRequest.getUserEmail());
    }

    @Test
    @DisplayName("Should verify repository interaction for successful login")
    void testLogin_VerifyRepositoryInteraction_Success() throws UserNotFoundException, InvalidPasswordException {
        // Arrange
        String mockToken = "mock.jwt.token";
        when(userRepository.findByUserEmail(validLoginRequest.getUserEmail()))
                .thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getUserPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(testUser)).thenReturn(mockToken);

        // Act
        loginService.login(validLoginRequest);

        // Assert - Verify repository, password encoder, and JWT util were called
        verify(userRepository, times(1)).findByUserEmail(validLoginRequest.getUserEmail());
        verify(passwordEncoder, times(1)).matches("password123", testUser.getUserPassword());
        verify(jwtTokenUtil, times(1)).generateToken(testUser);
        verify(userRepository, never()).findAll();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should verify repository interaction for user not found")
    void testLogin_VerifyRepositoryInteraction_UserNotFound() {
        // Arrange
        when(userRepository.findByUserEmail(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> loginService.login(validLoginRequest));

        // Assert - Verify repository was called exactly once
        verify(userRepository, times(1)).findByUserEmail(validLoginRequest.getUserEmail());
        verifyNoMoreInteractions(userRepository);
    }
}

