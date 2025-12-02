package com.automotive.login.security;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.automotive.login.entity.Customer;
import com.automotive.login.entity.Role;
import com.automotive.login.entity.User;
import com.automotive.login.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT Token Security Tests")
class JwtTokenSecurityTest {

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    private User testUser;
    private String validToken;

    @BeforeEach
    void setUp() {
        // Set secret key for testing
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", 
            "your-256-bit-secret-key-for-jwt-authentication-in-automotive-service-center");
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 86400000L); // 24 hours

        testUser = new Customer();
        testUser.setUserId(1L);
        testUser.setUserEmail("test@example.com");
        testUser.setUserName("Test User");
        testUser.setRole(Role.CUSTOMER);

        // Generate a valid token for testing
        validToken = jwtTokenUtil.generateToken(testUser);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken_ValidToken() {
        // Act
        String token = jwtTokenUtil.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should validate token is not expired")
    void testValidateToken_NotExpired() {
        // Act
        Boolean isValid = jwtTokenUtil.validateToken(validToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should detect expired token")
    void testValidateToken_Expired() {
        // Arrange - Set expiration to past
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", -1000L);
        String expiredToken = jwtTokenUtil.generateToken(testUser);
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 86400000L);

        // Act
        Boolean isValid = jwtTokenUtil.validateToken(expiredToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract user email from token")
    void testExtractUserEmail_FromToken() {
        // Act
        String email = jwtTokenUtil.getUsernameFromToken(validToken);

        // Assert
        assertNotNull(email);
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("Should extract user ID from token")
    void testExtractUserId_FromToken() {
        // Act
        Long userId = jwtTokenUtil.getUserIdFromToken(validToken);

        // Assert
        assertNotNull(userId);
        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("Should extract user role from token")
    void testExtractUserRole_FromToken() {
        // Act
        Role role = jwtTokenUtil.getRoleFromToken(validToken);

        // Assert
        assertNotNull(role);
        assertEquals(Role.CUSTOMER, role);
    }

    @Test
    @DisplayName("Should reject invalid token format")
    void testValidateToken_InvalidFormat() {
        // Arrange
        String invalidToken = "invalid.token.format";

        // Act
        Boolean isValid = jwtTokenUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject empty token")
    void testValidateToken_EmptyToken() {
        // Arrange
        String emptyToken = "";

        // Act
        Boolean isValid = jwtTokenUtil.validateToken(emptyToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should reject null token")
    void testValidateToken_NullToken() {
        // Act
        Boolean isValid = jwtTokenUtil.validateToken(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should validate token signature")
    void testValidateToken_SignatureValidation() {
        // Act
        Boolean isValid = jwtTokenUtil.validateToken(validToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should reject token with wrong secret key")
    void testValidateToken_WrongSecret() {
        // Arrange
        String token = jwtTokenUtil.generateToken(testUser);
        
        // Change secret key
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "different-secret-key");

        // Act
        Boolean isValid = jwtTokenUtil.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should check if token is expired")
    void testIsTokenExpired_NotExpired() {
        // Act
        Boolean isExpired = jwtTokenUtil.isTokenExpired(validToken);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should validate token contains all required claims")
    void testTokenContains_AllClaims() {
        // Act
        String email = jwtTokenUtil.getUsernameFromToken(validToken);
        Long userId = jwtTokenUtil.getUserIdFromToken(validToken);
        Role role = jwtTokenUtil.getRoleFromToken(validToken);

        // Assert
        assertNotNull(email);
        assertNotNull(userId);
        assertNotNull(role);
    }
}
