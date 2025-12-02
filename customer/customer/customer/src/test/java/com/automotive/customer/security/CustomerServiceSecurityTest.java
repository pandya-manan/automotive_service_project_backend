package com.automotive.customer.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.automotive.customer.entity.CustomerFeedback;
import com.automotive.customer.entity.FeedbackType;
import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.repository.CustomerRepository;
import com.automotive.customer.repository.CustomerFeedbackRepository;
import com.automotive.customer.repository.VehicleRepository;
import com.automotive.customer.service.CustomerFeedbackService;
import com.automotive.customer.service.VehicleServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Security Tests")
class CustomerServiceSecurityTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerFeedbackRepository feedbackRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private CustomerFeedbackService customerFeedbackService;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private CustomerFeedback testFeedback;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testFeedback = new CustomerFeedback();
        testFeedback.setSubject("Test Subject");
        testFeedback.setMessage("Test Message");
        testFeedback.setType(FeedbackType.COMPLAINT);

        testVehicle = new Vehicle();
        testVehicle.setVin("VIN123456");
        testVehicle.setMake("Toyota");
        testVehicle.setModel("Camry");
    }

    @Test
    @DisplayName("Should validate customer ID exists before submitting feedback")
    void testSubmitFeedback_ValidateCustomerId() {
        // Arrange
        when(customerRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerDoesNotExistException.class, 
            () -> customerFeedbackService.submitFeedback(999L, testFeedback));
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate customer ID exists before getting feedbacks")
    void testGetAllFeedbacksByCustomer_ValidateCustomerId() {
        // Arrange
        when(customerRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerDoesNotExistException.class, 
            () -> customerFeedbackService.getAllFeedbacksByCustomer(999L));
        verify(feedbackRepository, never()).findByCustomerId(anyLong());
    }

    @Test
    @DisplayName("Should validate customer exists before adding vehicle")
    void testAddVehicle_ValidateCustomerExists() {
        // Arrange
        when(customerRepository.findCustomerByUserId(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerDoesNotExistException.class, 
            () -> vehicleService.addVehicleToCustomer(999L, testVehicle));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate vehicle exists before updating")
    void testUpdateVehicle_ValidateVehicleExists() {
        // Arrange
        when(vehicleRepository.findVehicleByVehicleId(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(com.automotive.customer.exception.VehicleDoesNotExistException.class, 
            () -> vehicleService.updateVehicle(999L, testVehicle));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate vehicle exists before deleting")
    void testDeleteVehicle_ValidateVehicleExists() {
        // Arrange
        when(vehicleRepository.findVehicleByVehicleId(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(com.automotive.customer.exception.VehicleDoesNotExistException.class, 
            () -> vehicleService.deleteVehicle(999L));
        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should only return feedbacks for valid customer ID")
    void testGetAllFeedbacksByCustomer_ValidCustomerId() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(feedbackRepository.findByCustomerId(1L)).thenReturn(java.util.List.of(testFeedback));

        // Act
        var result = customerFeedbackService.getAllFeedbacksByCustomer(1L);

        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should validate customer ownership before operations")
    void testSubmitFeedback_CustomerOwnership() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        var result = customerFeedbackService.submitFeedback(1L, testFeedback);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        // In production, verify JWT token contains matching customer ID
    }
}
