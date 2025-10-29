package com.automotive.callcentre.service;

import com.automotive.callcentre.entity.*;
import com.automotive.callcentre.repository.CustomerFeedbackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CallCenterAgentService {

    @Autowired
    private CustomerFeedbackRepository feedbackRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String CUSTOMER_FEEDBACK_API = "http://localhost:9002/api/customers";

    // Get all feedbacks (synced from customer service)
    public List<CustomerFeedback> fetchFeedbacksFromCustomerService(Long customerId) {
        String url = CUSTOMER_FEEDBACK_API + "/" + customerId + "/feedbacks";
        CustomerFeedback[] fetched = restTemplate.getForObject(url, CustomerFeedback[].class);
        if (fetched != null) {
            for (CustomerFeedback f : fetched) {
                feedbackRepository.save(f); // store/update locally
            }
        }
        return feedbackRepository.findAll();
    }

    // Get all feedbacks in DB
    public List<CustomerFeedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // Assign an agent to a feedback
    public CustomerFeedback assignAgent(Long feedbackId, Long agentId) {
        CustomerFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setAssignedAgentId(agentId);
        feedback.setStatus(FeedbackStatus.IN_PROGRESS);
        feedback.setUpdatedAt(OffsetDateTime.now());
        return feedbackRepository.save(feedback);
    }

    // Add agent response
    public CustomerFeedback respondToFeedback(Long feedbackId, String response) {
        CustomerFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setAgentResponse(response);
        feedback.setStatus(FeedbackStatus.RESOLVED);
        feedback.setUpdatedAt(OffsetDateTime.now());
        return feedbackRepository.save(feedback);
    }

    // View all assigned feedbacks for a specific agent
    public List<CustomerFeedback> getFeedbacksByAgent(Long agentId) {
        return feedbackRepository.findByAssignedAgentId(agentId);
    }
}
