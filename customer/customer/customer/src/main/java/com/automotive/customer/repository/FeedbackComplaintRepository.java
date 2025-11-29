package com.automotive.customer.repository;

import com.automotive.customer.entity.FeedbackComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackComplaintRepository extends JpaRepository<FeedbackComplaint, Long> {
    List<FeedbackComplaint> findByStatus(FeedbackComplaint.Status status);
    List<FeedbackComplaint> findByCustomerId(Long customerId);
    List<FeedbackComplaint> findByServiceOrderId(String serviceOrderId);
}
