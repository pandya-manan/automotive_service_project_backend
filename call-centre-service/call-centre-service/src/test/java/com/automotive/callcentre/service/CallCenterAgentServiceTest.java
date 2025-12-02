package com.automotive.callcentre.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.web.client.RestTemplate;

import com.automotive.callcentre.entity.CustomerFeedback;
import com.automotive.callcentre.entity.FeedbackStatus;
import com.automotive.callcentre.repository.CustomerFeedbackRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CallCenterAgentService Tests")
class CallCenterAgentServiceTest {

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
        testFeedback.setType(com.automotive.callcentre.entity.FeedbackType.COMPLAINT);
        testFeedback.setStatus(FeedbackStatus.OPEN);
        testFeedback.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    @DisplayName("Should successfully get all feedbacks")
    void testGetAllFeedbacks_Success() {
        // Arrange
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        feedbacks.add(testFeedback);
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.getAllFeedbacks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFeedback.getId(), result.get(0).getId());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should successfully assign agent to feedback")
    void testAssignAgent_Success() {
        // Arrange
        Long agentId = 1L;
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        CustomerFeedback result = callCenterAgentService.assignAgent(1L, agentId);

        // Assert
        assertNotNull(result);
        assertEquals(FeedbackStatus.IN_PROGRESS, result.getStatus());
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when feedback not found for assignment")
    void testAssignAgent_FeedbackNotFound() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> callCenterAgentService.assignAgent(1L, 1L));
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully respond to feedback")
    void testRespondToFeedback_Success() {
        // Arrange
        String response = "We will look into this issue.";
        testFeedback.setStatus(FeedbackStatus.IN_PROGRESS);
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);

        // Act
        CustomerFeedback result = callCenterAgentService.respondToFeedback(1L, response);

        // Assert
        assertNotNull(result);
        assertEquals(FeedbackStatus.RESOLVED, result.getStatus());
        assertEquals(response, result.getAgentResponse());
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when feedback not found for response")
    void testRespondToFeedback_FeedbackNotFound() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> callCenterAgentService.respondToFeedback(1L, "Response"));
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully get feedbacks by agent")
    void testGetFeedbacksByAgent_Success() {
        // Arrange
        Long agentId = 1L;
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        testFeedback.setAssignedAgentId(agentId);
        feedbacks.add(testFeedback);
        when(feedbackRepository.findByAssignedAgentId(agentId)).thenReturn(feedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.getFeedbacksByAgent(agentId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(agentId, result.get(0).getAssignedAgentId());
        verify(feedbackRepository, times(1)).findByAssignedAgentId(agentId);
    }

    @Test
    @DisplayName("Should successfully fetch feedbacks from customer service")
    void testFetchFeedbacksFromCustomerService_Success() {
        // Arrange
        Long customerId = 1L;
        CustomerFeedback[] fetchedFeedbacks = new CustomerFeedback[]{testFeedback};
        List<CustomerFeedback> allFeedbacks = new ArrayList<>();
        allFeedbacks.add(testFeedback);
        
        when(restTemplate.getForObject(anyString(), eq(CustomerFeedback[].class)))
            .thenReturn(fetchedFeedbacks);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback);
        when(feedbackRepository.findAll()).thenReturn(allFeedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.fetchFeedbacksFromCustomerService(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CustomerFeedback[].class));
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle null response from customer service")
    void testFetchFeedbacksFromCustomerService_NullResponse() {
        // Arrange
        Long customerId = 1L;
        List<CustomerFeedback> existingFeedbacks = new ArrayList<>();
        existingFeedbacks.add(testFeedback);
        
        when(restTemplate.getForObject(anyString(), eq(CustomerFeedback[].class)))
            .thenReturn(null);
        when(feedbackRepository.findAll()).thenReturn(existingFeedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.fetchFeedbacksFromCustomerService(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CustomerFeedback[].class));
        verify(feedbackRepository, never()).save(any(CustomerFeedback.class));
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle empty array from customer service")
    void testFetchFeedbacksFromCustomerService_EmptyArray() {
        // Arrange
        Long customerId = 1L;
        CustomerFeedback[] emptyArray = new CustomerFeedback[0];
        List<CustomerFeedback> existingFeedbacks = new ArrayList<>();
        
        when(restTemplate.getForObject(anyString(), eq(CustomerFeedback[].class)))
            .thenReturn(emptyArray);
        when(feedbackRepository.findAll()).thenReturn(existingFeedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.fetchFeedbacksFromCustomerService(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CustomerFeedback[].class));
        verify(feedbackRepository, never()).save(any(CustomerFeedback.class));
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle multiple feedbacks from customer service")
    void testFetchFeedbacksFromCustomerService_MultipleFeedbacks() {
        // Arrange
        Long customerId = 1L;
        CustomerFeedback feedback2 = new CustomerFeedback();
        feedback2.setId(2L);
        feedback2.setCustomerId(1L);
        CustomerFeedback[] fetchedFeedbacks = new CustomerFeedback[]{testFeedback, feedback2};
        List<CustomerFeedback> allFeedbacks = new ArrayList<>();
        allFeedbacks.add(testFeedback);
        allFeedbacks.add(feedback2);
        
        when(restTemplate.getForObject(anyString(), eq(CustomerFeedback[].class)))
            .thenReturn(fetchedFeedbacks);
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenReturn(testFeedback, feedback2);
        when(feedbackRepository.findAll()).thenReturn(allFeedbacks);

        // Act
        List<CustomerFeedback> result = callCenterAgentService.fetchFeedbacksFromCustomerService(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate, times(1)).getForObject(anyString(), eq(CustomerFeedback[].class));
        verify(feedbackRepository, times(2)).save(any(CustomerFeedback.class));
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get empty list when no feedbacks exist")
    void testGetAllFeedbacks_EmptyList() {
        // Arrange
        when(feedbackRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<CustomerFeedback> result = callCenterAgentService.getAllFeedbacks();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get empty list when no feedbacks for agent")
    void testGetFeedbacksByAgent_EmptyList() {
        // Arrange
        Long agentId = 1L;
        when(feedbackRepository.findByAssignedAgentId(agentId)).thenReturn(new ArrayList<>());

        // Act
        List<CustomerFeedback> result = callCenterAgentService.getFeedbacksByAgent(agentId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(feedbackRepository, times(1)).findByAssignedAgentId(agentId);
    }

    @Test
    @DisplayName("Should update updatedAt when assigning agent")
    void testAssignAgent_UpdatesTimestamp() {
        // Arrange
        Long agentId = 1L;
        OffsetDateTime beforeTime = OffsetDateTime.now();
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback saved = invocation.getArgument(0);
            assertNotNull(saved.getUpdatedAt());
            return saved;
        });

        // Act
        CustomerFeedback result = callCenterAgentService.assignAgent(1L, agentId);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
    }

    @Test
    @DisplayName("Should update updatedAt when responding to feedback")
    void testRespondToFeedback_UpdatesTimestamp() {
        // Arrange
        String response = "Response text";
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(testFeedback));
        when(feedbackRepository.save(any(CustomerFeedback.class))).thenAnswer(invocation -> {
            CustomerFeedback saved = invocation.getArgument(0);
            assertNotNull(saved.getUpdatedAt());
            return saved;
        });

        // Act
        CustomerFeedback result = callCenterAgentService.respondToFeedback(1L, response);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUpdatedAt());
        verify(feedbackRepository, times(1)).save(any(CustomerFeedback.class));
    }
}


