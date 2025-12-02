package com.automotive.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.automotive.customer.entity.CustomerFeedback;
import com.automotive.customer.entity.FeedbackStatus;
import com.automotive.customer.entity.FeedbackType;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.repository.CustomerFeedbackRepository;
import com.automotive.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerFeedbackService Tests")
class CustomerFeedbackServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerFeedbackRepository feedbackRepository;

    @InjectMocks
    private CustomerFeedbackService customerFeedbackService;

    private CustomerFeedback testFeedback;
    private Long testCustomerId;

    @BeforeEach
    void setUp() {
        testCustomerId = 1L;
        testFeedback = new CustomerFeedback();
        testFeedback.setId(1L);
        testFeedback.setCustomerId(testCustomerId);
        testFeedback.setSubject("Test Subject");
        testFeedback.setMessage("Test Message");
        testFeedback.setType(FeedbackType.COMPLAINT);
        testFeedback.setStatus(FeedbackStatus.OPEN);
    }

    @Test
    @DisplayName("Should successfully submit feedback")
    void testSubmitFeedback_Success() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            feedback.setId(1L);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertNotNull(result);
        assertEquals(testCustomerId, result.getCustomerId());
        assertEquals(FeedbackStatus.OPEN, result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        verify(customerRepository, times(1)).existsById(testCustomerId);
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
    }

    @Test
    @DisplayName("Should throw CustomerDoesNotExistException when customer not found")
    void testSubmitFeedback_CustomerNotFound() {
        // Arrange
        when(customerRepository.existsById(testCustomerId)).thenReturn(false);

        // Act & Assert
        CustomerDoesNotExistException exception = assertThrows(
            CustomerDoesNotExistException.class,
            () -> customerFeedbackService.submitFeedback(testCustomerId, testFeedback)
        );

        assertEquals("Customer not found with ID: " + testCustomerId, exception.getMessage());
        verify(customerRepository, times(1)).existsById(testCustomerId);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully get all feedbacks by customer")
    void testGetAllFeedbacksByCustomer_Success() throws CustomerDoesNotExistException {
        // Arrange
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        feedbacks.add(testFeedback);
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.findByCustomerId(testCustomerId)).thenReturn(feedbacks);

        // Act
        List<CustomerFeedback> result = customerFeedbackService.getAllFeedbacksByCustomer(testCustomerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFeedback.getId(), result.get(0).getId());
        verify(customerRepository, times(1)).existsById(testCustomerId);
        verify(feedbackRepository, times(1)).findByCustomerId(testCustomerId);
    }

    @Test
    @DisplayName("Should throw CustomerDoesNotExistException when getting feedbacks for non-existent customer")
    void testGetAllFeedbacksByCustomer_CustomerNotFound() {
        // Arrange
        when(customerRepository.existsById(testCustomerId)).thenReturn(false);

        // Act & Assert
        CustomerDoesNotExistException exception = assertThrows(
            CustomerDoesNotExistException.class,
            () -> customerFeedbackService.getAllFeedbacksByCustomer(testCustomerId)
        );

        assertEquals("Customer not found with ID: " + testCustomerId, exception.getMessage());
        verify(customerRepository, times(1)).existsById(testCustomerId);
        verify(feedbackRepository, never()).findByCustomerId(any());
    }

    @Test
    @DisplayName("Should return empty list when customer has no feedbacks")
    void testGetAllFeedbacksByCustomer_EmptyList() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.findByCustomerId(testCustomerId)).thenReturn(new ArrayList<>());

        // Act
        List<CustomerFeedback> result = customerFeedbackService.getAllFeedbacksByCustomer(testCustomerId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(customerRepository, times(1)).existsById(testCustomerId);
        verify(feedbackRepository, times(1)).findByCustomerId(testCustomerId);
    }

    @Test
    @DisplayName("Should successfully get feedback by ID")
    void testGetFeedbackById_Success() {
        // Arrange
        Long feedbackId = 1L;
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(testFeedback));

        // Act
        CustomerFeedback result = customerFeedbackService.getFeedbackById(feedbackId);

        // Assert
        assertNotNull(result);
        assertEquals(feedbackId, result.getId());
        verify(feedbackRepository, times(1)).findById(feedbackId);
    }

    @Test
    @DisplayName("Should return null when feedback not found")
    void testGetFeedbackById_NotFound() {
        // Arrange
        Long feedbackId = 999L;
        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.empty());

        // Act
        CustomerFeedback result = customerFeedbackService.getFeedbackById(feedbackId);

        // Assert
        assertNull(result);
        verify(feedbackRepository, times(1)).findById(feedbackId);
    }

    @Test
    @DisplayName("Should set correct status when submitting feedback")
    void testSubmitFeedback_SetsCorrectStatus() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertEquals(FeedbackStatus.OPEN, result.getStatus());
    }

    @Test
    @DisplayName("Should set customer ID when submitting feedback")
    void testSubmitFeedback_SetsCustomerId() throws CustomerDoesNotExistException {
        // Arrange
        testFeedback.setCustomerId(null);
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertEquals(testCustomerId, result.getCustomerId());
    }

    @Test
    @DisplayName("Should set createdAt timestamp when submitting feedback")
    void testSubmitFeedback_SetsCreatedAt() throws CustomerDoesNotExistException {
        // Arrange
        OffsetDateTime beforeTime = OffsetDateTime.now();
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertNotNull(result.getCreatedAt());
        assertTrue(result.getCreatedAt().isAfter(beforeTime.minusSeconds(1)) || 
                   result.getCreatedAt().isEqual(beforeTime));
    }

    @Test
    @DisplayName("Should set updatedAt to null when submitting feedback")
    void testSubmitFeedback_SetsUpdatedAtToNull() throws CustomerDoesNotExistException {
        // Arrange
        testFeedback.setUpdatedAt(OffsetDateTime.now());
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle multiple feedbacks for same customer")
    void testGetAllFeedbacksByCustomer_MultipleFeedbacks() throws CustomerDoesNotExistException {
        // Arrange
        CustomerFeedback feedback2 = new CustomerFeedback();
        feedback2.setId(2L);
        feedback2.setCustomerId(testCustomerId);
        feedback2.setType(FeedbackType.FEEDBACK);
        
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        feedbacks.add(testFeedback);
        feedbacks.add(feedback2);
        
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.findByCustomerId(testCustomerId)).thenReturn(feedbacks);

        // Act
        List<CustomerFeedback> result = customerFeedbackService.getAllFeedbacksByCustomer(testCustomerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(feedbackRepository, times(1)).findByCustomerId(testCustomerId);
    }

    @Test
    @DisplayName("Should handle different feedback types")
    void testSubmitFeedback_DifferentTypes() throws CustomerDoesNotExistException {
        // Arrange
        testFeedback.setType(FeedbackType.FEEDBACK);
        when(customerRepository.existsById(testCustomerId)).thenReturn(true);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback feedback = invocation.getArgument(0);
            return feedback;
        });

        // Act
        CustomerFeedback result = customerFeedbackService.submitFeedback(testCustomerId, testFeedback);

        // Assert
        assertEquals(FeedbackType.FEEDBACK, result.getType());
        assertEquals(FeedbackStatus.OPEN, result.getStatus());
    }
}

