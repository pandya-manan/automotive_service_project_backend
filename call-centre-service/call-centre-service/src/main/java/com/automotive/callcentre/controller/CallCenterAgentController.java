package com.automotive.callcentre.controller;

import com.automotive.callcentre.entity.*;
import com.automotive.callcentre.service.CallCenterAgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Call Centre Agent Service",
        description = "APIs for viewing and responding to customer complaints and feedbacks.")
@RestController
@RequestMapping("/api/callcenter/agents")
public class CallCenterAgentController {

    @Autowired
    private CallCenterAgentService agentService;

    @Operation(summary = "Sync all feedbacks from customer service for a given customer")
    @GetMapping("/sync/{customerId}")
    public ResponseEntity<List<CustomerFeedback>> syncFeedbacks(@PathVariable Long customerId) {
        List<CustomerFeedback> synced = agentService.fetchFeedbacksFromCustomerService(customerId);
        return ResponseEntity.ok(synced);
    }

    @Operation(summary = "View all feedbacks stored in this service")
    @GetMapping("/feedbacks")
    public ResponseEntity<List<CustomerFeedback>> getAllFeedbacks() {
        return ResponseEntity.ok(agentService.getAllFeedbacks());
    }

    @Operation(summary = "Assign agent to a feedback")
    @PutMapping("/{agentId}/feedbacks/{feedbackId}/assign")
    public ResponseEntity<CustomerFeedback> assignFeedback(
            @PathVariable Long agentId,
            @PathVariable Long feedbackId) {
        CustomerFeedback updated = agentService.assignAgent(feedbackId, agentId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Agent responds to a feedback")
    @PutMapping("/{agentId}/feedbacks/{feedbackId}/respond")
    public ResponseEntity<CustomerFeedback> respondToFeedback(
            @PathVariable Long agentId,
            @PathVariable Long feedbackId,
            @RequestBody String response) {
        CustomerFeedback updated = agentService.respondToFeedback(feedbackId, response);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "View feedbacks assigned to a specific agent")
    @GetMapping("/{agentId}/feedbacks")
    public ResponseEntity<List<CustomerFeedback>> getAgentFeedbacks(@PathVariable Long agentId) {
        return ResponseEntity.ok(agentService.getFeedbacksByAgent(agentId));
    }
}
