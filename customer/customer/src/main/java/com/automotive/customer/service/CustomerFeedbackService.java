package com.automotive.customer.service;


import com.automotive.customer.entity.*;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.repository.CustomerFeedbackRepository;
import com.automotive.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CustomerFeedbackService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerFeedbackRepository feedbackRepository;

    public CustomerFeedback submitFeedback(Long customerId, CustomerFeedback feedback)
            throws CustomerDoesNotExistException {

        if (!customerRepository.existsById(customerId)) {
            throw new CustomerDoesNotExistException("Customer not found with ID: " + customerId);
        }

        feedback.setCustomerId(customerId);
        feedback.setStatus(FeedbackStatus.OPEN);
        feedback.setCreatedAt(OffsetDateTime.now());
        feedback.setUpdatedAt(null);

        return feedbackRepository.save(feedback);
    }

    public List<CustomerFeedback> getAllFeedbacksByCustomer(Long customerId)
            throws CustomerDoesNotExistException {

        if (!customerRepository.existsById(customerId)) {
            throw new CustomerDoesNotExistException("Customer not found with ID: " + customerId);
        }

        return feedbackRepository.findByCustomerId(customerId);
    }

    public CustomerFeedback getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElse(null);
    }
}
