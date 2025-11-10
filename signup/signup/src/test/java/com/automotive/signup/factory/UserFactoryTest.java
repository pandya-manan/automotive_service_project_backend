package com.automotive.signup.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.*;

@DisplayName("UserFactory Tests")
class UserFactoryTest {

    private SignUpRequestDTO signUpRequestDTO;

    @BeforeEach
    void setUp() {
        signUpRequestDTO = new SignUpRequestDTO();
        signUpRequestDTO.setUserName("Test User");
        signUpRequestDTO.setUserEmail("test@example.com");
        signUpRequestDTO.setUserPassword("password123");
        signUpRequestDTO.setUserPhoneNumber("1234567890");
        signUpRequestDTO.setUserAddress("123 Test Street, Test City, Test State, 12345");
        signUpRequestDTO.setUserImageUrl("http://example.com/image.jpg");
    }

    @Test
    @DisplayName("Should create Customer user successfully")
    void testCreateUser_Customer() {
        // Arrange
        signUpRequestDTO.setRole("CUSTOMER");

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof Customer);
        assertEquals("Test User", user.getUserName());
        assertEquals("test@example.com", user.getUserEmail());
        assertEquals("password123", user.getUserPassword());
        assertEquals("1234567890", user.getUserPhoneNumber());
        assertEquals("123 Test Street, Test City, Test State, 12345", user.getUserAddress());
        assertEquals(Role.CUSTOMER, user.getRole());
        assertEquals("http://example.com/image.jpg", user.getUserImageUrl());
        assertEquals("CUSTOMER", user.getType());
    }

    @Test
    @DisplayName("Should create ServiceManager user successfully")
    void testCreateUser_ServiceManager() {
        // Arrange
        signUpRequestDTO.setRole("SERVICE_MANAGER");
        signUpRequestDTO.setYearsOfExperience(5);
        signUpRequestDTO.setServiceDepartment("Engine Service");

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof ServiceManager);
        assertEquals("Test User", user.getUserName());
        assertEquals(Role.SERVICE_MANAGER, user.getRole());
        ServiceManager manager = (ServiceManager) user;
        assertEquals(5, manager.getYearsOfExperience());
        assertEquals("Engine Service", manager.getServiceDepartment());
    }

    @Test
    @DisplayName("Should create Admin user successfully")
    void testCreateUser_Admin() {
        // Arrange
        signUpRequestDTO.setRole("ADMIN");

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof AdminUser);
        assertEquals("Test User", user.getUserName());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Should create Mechanic user successfully")
    void testCreateUser_Mechanic() {
        // Arrange
        signUpRequestDTO.setRole("MECHANIC");
        signUpRequestDTO.setYearsOfExperience(3);
        signUpRequestDTO.setSpecialization("ENGINE");
        signUpRequestDTO.setHourlyRate(50.0);

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof Mechanic);
        assertEquals("Test User", user.getUserName());
        assertEquals(Role.MECHANIC, user.getRole());
        Mechanic mechanic = (Mechanic) user;
        assertEquals(3, mechanic.getYearsOfExperience());
        assertEquals(Specialization.ENGINE, mechanic.getSpecialization());
        assertEquals(50.0, mechanic.getHourlyRate());
        assertEquals(AvailabilityStatus.AVAILABLE, mechanic.getAvailabilityStatus());
    }

    @Test
    @DisplayName("Should create CallCenterAgent user successfully")
    void testCreateUser_CallCentreAgent() {
        // Arrange
        signUpRequestDTO.setRole("CALL_CENTRE_AGENT");

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof CallCenterAgent);
        assertEquals("Test User", user.getUserName());
        assertEquals(Role.CALL_CENTRE_AGENT, user.getRole());
    }

    @Test
    @DisplayName("Should handle case-insensitive role input")
    void testCreateUser_CaseInsensitiveRole() {
        // Arrange
        signUpRequestDTO.setRole("customer"); // lowercase

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertTrue(user instanceof Customer);
        assertEquals(Role.CUSTOMER, user.getRole());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for unsupported role")
    void testCreateUser_UnsupportedRole() {
        // Arrange
        signUpRequestDTO.setRole("INVALID_ROLE");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> UserFactory.createUser(signUpRequestDTO)
        );

        assertNotNull(exception.getMessage());
        // The exception message should contain information about the invalid role
        assertTrue(exception.getMessage().contains("INVALID_ROLE") || 
                   exception.getMessage().contains("Unsupported role") ||
                   exception.getMessage().contains("No enum constant"));
    }

    @Test
    @DisplayName("Should populate all common fields correctly")
    void testCreateUser_PopulateCommonFields() {
        // Arrange
        signUpRequestDTO.setRole("CUSTOMER");

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertEquals("Test User", user.getUserName());
        assertEquals("test@example.com", user.getUserEmail());
        assertEquals("password123", user.getUserPassword());
        assertEquals("1234567890", user.getUserPhoneNumber());
        assertEquals("123 Test Street, Test City, Test State, 12345", user.getUserAddress());
        assertEquals("http://example.com/image.jpg", user.getUserImageUrl());
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void testCreateUser_NullOptionalFields() {
        // Arrange
        signUpRequestDTO.setRole("CUSTOMER");
        signUpRequestDTO.setUserImageUrl(null);

        // Act
        User user = UserFactory.createUser(signUpRequestDTO);

        // Assert
        assertNotNull(user);
        assertNull(user.getUserImageUrl());
    }

    @Test
    @DisplayName("Should handle different specializations for mechanic")
    void testCreateUser_MechanicDifferentSpecializations() {
        // Test different specializations
        String[] specializations = {"ENGINE", "ELECTRICAL", "BODYWORK", "DIAGNOSTICS", "AC", "GENERAL"};
        
        for (String spec : specializations) {
            // Arrange
            signUpRequestDTO.setRole("MECHANIC");
            signUpRequestDTO.setSpecialization(spec);

            // Act
            User user = UserFactory.createUser(signUpRequestDTO);

            // Assert
            assertNotNull(user);
            assertTrue(user instanceof Mechanic);
            Mechanic mechanic = (Mechanic) user;
            assertEquals(Specialization.valueOf(spec), mechanic.getSpecialization());
        }
    }
}

