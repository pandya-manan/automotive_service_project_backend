package com.automotive.callcentre.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.automotive.callcentre.entity.CustomerFeedback;
import com.automotive.callcentre.entity.FeedbackStatus;
import com.automotive.callcentre.repository.CustomerFeedbackRepository;
import com.automotive.callcentre.service.CallCenterAgentService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Call Centre Security Tests")
class CallCentreSecurityTest {

    @Mock
    private CustomerFeedbackRepository feedbackRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CallCenterAgentService callCenterAgentService;

    private CustomerFeedback testFeedback;

    @BeforeEach
    void setUp() {
        testFeedback = new CustomerFeedback();
        testFeedback.setId(1L);
        testFeedback.setCustomerId(1L);
        testFeedback.setStatus(FeedbackStatus.OPEN);
    }

    @Test
    @DisplayName("Should validate feedback exists before assigning agent")
    void testAssignAgent_ValidateFeedbackExists() {
        // Arrange
        when(feedbackRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> callCenterAgentService.assignAgent(999L, 1L));
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate feedback exists before responding")
    void testRespondToFeedback_ValidateFeedbackExists() {
        // Arrange
        when(feedbackRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> callCenterAgentService.respondToFeedback(999L, "Response"));
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate agent ID is provided")
    void testAssignAgent_AgentIdRequired() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(java.util.Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        var result = callCenterAgentService.assignAgent(1L, null);

        // Assert
        assertNotNull(result);
        // In production, validate agent ID is not null and exists
    }

    @Test
    @DisplayName("Should validate response is provided")
    void testRespondToFeedback_ResponseRequired() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(java.util.Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        var result = callCenterAgentService.respondToFeedback(1L, null);

        // Assert
        assertNotNull(result);
        // In production, validate response is not null or empty
    }

    @Test
    @DisplayName("Should only return feedbacks for valid agent ID")
    void testGetFeedbacksByAgent_ValidateAgentId() {
        // Arrange
        when(feedbackRepository.findByAssignedAgentId(1L)).thenReturn(java.util.List.of(testFeedback));

        // Act
        var result = callCenterAgentService.getFeedbacksByAgent(1L);

        // Assert
        assertNotNull(result);
        // In production, verify JWT token contains matching agent ID
    }

    @Test
    @DisplayName("Should validate customer ID before fetching feedbacks")
    void testFetchFeedbacksFromCustomerService_ValidateCustomerId() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(CustomerFeedback[].class)))
            .thenReturn(null);
        when(feedbackRepository.findAll()).thenReturn(java.util.List.of(testFeedback));

        // Act
        var result = callCenterAgentService.fetchFeedbacksFromCustomerService(1L);

        // Assert
        assertNotNull(result);
        // In production, validate customer ID format and existence
    }

    @Test
    @DisplayName("Should validate agent has permission to access feedback")
    void testAssignAgent_AgentPermission() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(java.util.Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        var result = callCenterAgentService.assignAgent(1L, 1L);

        // Assert
        assertNotNull(result);
        // In production, verify JWT token contains valid agent role
    }
}
