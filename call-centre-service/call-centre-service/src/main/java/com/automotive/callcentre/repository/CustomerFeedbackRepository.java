package com.automotive.callcentre.repository;

import com.automotive.callcentre.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Long> {
    List<CustomerFeedback> findByAssignedAgentId(Long agentId);
    List<CustomerFeedback> findByStatus(String status);
}
