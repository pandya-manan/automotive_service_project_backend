package com.automotive.customer.controller;

import com.automotive.customer.entity.CustomerFeedback;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.service.CustomerFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Customer Feedback and Complaint Service",
        description = "APIs for submitting complaints and feedback after service completion.")
@RestController
@RequestMapping("/api/customers/{customerId}/feedbacks")
public class CustomerFeedbackController {

    @Autowired
    private CustomerFeedbackService feedbackService;

    @Operation(summary = "Submit new feedback or complaint",
            description = "Allows customer to submit feedback or complaint after service completion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feedback or complaint submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<CustomerFeedback> submitFeedback(@PathVariable Long customerId,
                                                           @RequestBody CustomerFeedback feedback)
            throws CustomerDoesNotExistException {
        CustomerFeedback saved = feedbackService.submitFeedback(customerId, feedback);
        URI location = URI.create(String.format("/api/customers/%d/feedbacks/%d", customerId, saved.getId()));
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(summary = "View all feedback/complaints of a customer",
            description = "Fetches all submitted complaints or feedback for a customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<CustomerFeedback>> getAllFeedbacks(@PathVariable Long customerId)
            throws CustomerDoesNotExistException {
        List<CustomerFeedback> feedbacks = feedbackService.getAllFeedbacksByCustomer(customerId);
        return ResponseEntity.ok(feedbacks);
    }

    @Operation(summary = "Get feedback by ID",
            description = "Fetch a single feedback or complaint record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    @GetMapping("/{feedbackId}")
    public ResponseEntity<CustomerFeedback> getFeedbackById(@PathVariable Long customerId,
                                                            @PathVariable Long feedbackId)
            throws CustomerDoesNotExistException {
        CustomerFeedback feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback == null || !feedback.getCustomerId().equals(customerId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(feedback);
    }
}
