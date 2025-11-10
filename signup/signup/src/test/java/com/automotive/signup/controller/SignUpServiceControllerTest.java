package com.automotive.signup.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.Customer;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.service.SignUpService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignUpServiceController Tests")
class SignUpServiceControllerTest {

    @Mock
    private SignUpService signUpService;

    @InjectMocks
    private SignUpServiceController signUpServiceController;

    private SignUpRequestDTO signUpRequestDTO;
    private User savedUser;

    @BeforeEach
    void setUp() {
        signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUserName("Test User");
        signUpRequestDTO.setUserEmail("test@example.com");
        signUpRequestDTO.setUserPassword("password123");
        signUpRequestDTO.setUserPhoneNumber("1234567890");
        signUpRequestDTO.setUserAddress("123 Test Street, Test City, Test State, 12345");
        signUpRequestDTO.setRole("CUSTOMER");

        savedUser = new Customer();
        savedUser.setUserId(1L);
        savedUser.setUserName("Test User");
        savedUser.setUserEmail("test@example.com");
        savedUser.setUserPassword("password123");
        savedUser.setUserPhoneNumber("1234567890");
        savedUser.setUserAddress("123 Test Street, Test City, Test State, 12345");
    }

    @Test
    @DisplayName("Should successfully register user and return 201 status")
    void testRegisterUser_Success() throws UserAlreadyExistsException {
        // Arrange
        when(signUpService.registerUser(any(SignUpRequestDTO.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<User> response = signUpServiceController.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test User", response.getBody().getUserName());
        assertEquals("test@example.com", response.getBody().getUserEmail());
        verify(signUpService, times(1)).registerUser(any(SignUpRequestDTO.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void testRegisterUser_UserAlreadyExists() throws UserAlreadyExistsException {
        // Arrange
        when(signUpService.registerUser(any(SignUpRequestDTO.class)))
                .thenThrow(new UserAlreadyExistsException("Email already in use!"));

        // Act & Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpServiceController.registerUser(signUpRequestDTO)
        );

        verify(signUpService, times(1)).registerUser(any(SignUpRequestDTO.class));
    }

    @Test
    @DisplayName("Should handle different user roles successfully")
    void testRegisterUser_DifferentRoles() throws UserAlreadyExistsException {
        // Test with SERVICE_MANAGER role
        signUpRequestDTO.setRole("SERVICE_MANAGER");
        signUpRequestDTO.setYearsOfExperience(5);
        signUpRequestDTO.setServiceDepartment("Engine Service");

        when(signUpService.registerUser(any(SignUpRequestDTO.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<User> response = signUpServiceController.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(signUpService, times(1)).registerUser(any(SignUpRequestDTO.class));
    }

    @Test
    @DisplayName("Should verify service interaction")
    void testRegisterUser_VerifyServiceInteraction() throws UserAlreadyExistsException {
        // Arrange
        when(signUpService.registerUser(any(SignUpRequestDTO.class))).thenReturn(savedUser);

        // Act
        signUpServiceController.registerUser(signUpRequestDTO);

        // Assert
        verify(signUpService, times(1)).registerUser(any(SignUpRequestDTO.class));
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    @DisplayName("Should return correct response body with user details")
    void testRegisterUser_ResponseBody() throws UserAlreadyExistsException {
        // Arrange
        when(signUpService.registerUser(any(SignUpRequestDTO.class))).thenReturn(savedUser);

        // Act
        ResponseEntity<User> response = signUpServiceController.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(response.getBody());
        User responseUser = response.getBody();
        assertEquals(1L, responseUser.getUserId());
        assertEquals("Test User", responseUser.getUserName());
        assertEquals("test@example.com", responseUser.getUserEmail());
        assertEquals("1234567890", responseUser.getUserPhoneNumber());
    }

    @Test
    @DisplayName("Should handle phone number already exists exception")
    void testRegisterUser_PhoneNumberExists() throws UserAlreadyExistsException {
        // Arrange
        when(signUpService.registerUser(any(SignUpRequestDTO.class)))
                .thenThrow(new UserAlreadyExistsException("Phone number already in use!"));

        // Act & Assert
        assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpServiceController.registerUser(signUpRequestDTO)
        );

        verify(signUpService, times(1)).registerUser(any(SignUpRequestDTO.class));
    }
}


