package com.automotive.signup.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_feedback")
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;          // FK reference to User (CUSTOMER)
    private String serviceOrderId;    // Optional - if feedback/complaint is about a specific work order
    private String subject;
    private String message;
    
    @Enumerated(EnumType.STRING)
    private FeedbackType type;        // FEEDBACK or COMPLAINT

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;    // OPEN, IN_PROGRESS, RESOLVED

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt;

    private Long assignedAgentId;     // Who picked it up (Call Center Agent)

    @Column(length = 2000)
    private String agentResponse;     // Agent’s reply if any
}
