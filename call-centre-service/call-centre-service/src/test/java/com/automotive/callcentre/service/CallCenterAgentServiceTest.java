package com.automotive.callcentre.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
}


