package com.automotive.signup.service;

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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.Customer;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("SignUpServiceImpl Tests")
class SignUpServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private org.springframework.http.ResponseEntity<String> responseEntity;

    @InjectMocks
    private SignUpServiceImpl signUpService;

    private SignUpRequestDTO signUpRequestDTO;
    private User savedUser;

    @BeforeEach
    void setUp() {
        // Set email service URL using reflection
        ReflectionTestUtils.setField(signUpService, "EMAIL_SERVICE_URL", "http://localhost:9003/api/email/signup");

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
        savedUser.setUserPassword("$2a$10$encryptedPasswordHash"); // Encrypted password
        savedUser.setUserPhoneNumber("1234567890");
        savedUser.setUserAddress("123 Test Street, Test City, Test State, 12345");
    }

    @Test
    @DisplayName("Should successfully register a new customer")
    void testRegisterUser_Success_Customer() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getUserName());
        assertEquals("test@example.com", result.getUserEmail());
        verify(userRepository, times(1)).existsByUserEmail("test@example.com");
        verify(userRepository, times(1)).existsByUserPhoneNumber("1234567890");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email already exists")
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpService.registerUser(signUpRequestDTO)
        );

        assertEquals("Email already in use!", exception.getMessage());
        verify(userRepository, times(1)).existsByUserEmail("test@example.com");
        verify(userRepository, never()).existsByUserPhoneNumber(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when phone number already exists")
    void testRegisterUser_PhoneNumberAlreadyExists() {
        // Arrange
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> signUpService.registerUser(signUpRequestDTO)
        );

        assertEquals("Phone number already in use!", exception.getMessage());
        verify(userRepository, times(1)).existsByUserEmail("test@example.com");
        verify(userRepository, times(1)).existsByUserPhoneNumber("1234567890");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully register user even if email service fails")
    void testRegisterUser_EmailServiceFailure() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient to throw exception
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenThrow(new RuntimeException("Email service unavailable"));

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getUserName());
        // User registration should succeed even if email fails
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully register a service manager")
    void testRegisterUser_Success_ServiceManager() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        signUpRequestDTO.setRole("SERVICE_MANAGER");
        signUpRequestDTO.setYearsOfExperience(5);
        signUpRequestDTO.setServiceDepartment("Engine Service");

        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully register a mechanic")
    void testRegisterUser_Success_Mechanic() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        signUpRequestDTO.setRole("MECHANIC");
        signUpRequestDTO.setYearsOfExperience(3);
        signUpRequestDTO.setSpecialization("ENGINE");
        signUpRequestDTO.setHourlyRate(50.0);

        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully register an admin user")
    void testRegisterUser_Success_Admin() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        signUpRequestDTO.setRole("ADMIN");

        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should successfully register a call centre agent")
    void testRegisterUser_Success_CallCentreAgent() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        signUpRequestDTO.setRole("CALL_CENTRE_AGENT");

        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        User result = signUpService.registerUser(signUpRequestDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should verify repository interaction for successful registration")
    void testRegisterUser_VerifyRepositoryInteraction() throws UserAlreadyExistsException {
        // Arrange
        String encryptedPassword = "$2a$10$encryptedPasswordHash";
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn(encryptedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        // Mock RestClient chain
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(responseEntity);

        // Act
        signUpService.registerUser(signUpRequestDTO);

        // Assert
        verify(userRepository, times(1)).existsByUserEmail("test@example.com");
        verify(userRepository, times(1)).existsByUserPhoneNumber("1234567890");
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, never()).findAll();
        verify(userRepository, never()).delete(any());
    }
}

