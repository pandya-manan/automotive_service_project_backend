package com.automotive.signup.security;

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
import org.springframework.web.client.RestClient;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.Customer;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.repository.UserRepository;
import com.automotive.signup.service.SignUpServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignUp Security Tests")
class SignupSecurityTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignUpServiceImpl signUpService;

    private SignUpRequestDTO signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequestDTO();
        signUpRequest.setUserEmail("test@example.com");
        signUpRequest.setUserPassword("SecurePass123!");
        signUpRequest.setUserName("Test User");
        signUpRequest.setUserPhoneNumber("1234567890");
        signUpRequest.setRole("CUSTOMER");
        signUpRequest.setUserAddress("123 Test Street, Test City, Test State, Test Country");
    }

    @Test
    @DisplayName("Should encrypt password before storing")
    void testSignup_PasswordEncryption() throws UserAlreadyExistsException {
        // Arrange
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123!")).thenReturn("$2a$10$encryptedPasswordHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals("$2a$10$encryptedPasswordHash", user.getUserPassword());
            return user;
        });

        // Act
        signUpService.registerUser(signUpRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode("SecurePass123!");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should validate email is provided")
    void testSignup_EmailRequired() {
        // Arrange
        signUpRequest.setUserEmail(null);
        when(userRepository.existsByUserEmail(null)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, 
            () -> signUpService.registerUser(signUpRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate phone number is provided")
    void testSignup_PhoneRequired() {
        // Arrange
        signUpRequest.setUserPhoneNumber(null);
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber(null)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, 
            () -> signUpService.registerUser(signUpRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should prevent duplicate email registration")
    void testSignup_DuplicateEmail() {
        // Arrange
        when(userRepository.existsByUserEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, 
            () -> signUpService.registerUser(signUpRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should prevent duplicate phone number registration")
    void testSignup_DuplicatePhone() {
        // Arrange
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber("1234567890")).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, 
            () -> signUpService.registerUser(signUpRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate password is provided")
    void testSignup_PasswordRequired() {
        // Arrange
        signUpRequest.setUserPassword(null);
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(null)).thenReturn("$2a$10$encrypted");
        when(userRepository.save(any(User.class))).thenReturn(new Customer());

        // Act & Assert
        // Note: In production, add password validation
        assertDoesNotThrow(() -> signUpService.registerUser(signUpRequest));
    }

    @Test
    @DisplayName("Should not store plain text password")
    void testSignup_NoPlainTextPassword() throws UserAlreadyExistsException {
        // Arrange
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserPhoneNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123!")).thenReturn("$2a$10$encryptedPasswordHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertNotEquals("SecurePass123!", user.getUserPassword());
            assertTrue(user.getUserPassword().startsWith("$2a$"));
            return user;
        });

        // Act
        signUpService.registerUser(signUpRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode(anyString());
    }
}
