package com.automotive.email.service;

import com.automotive.email.entity.ServiceBookingRequestDTO;
import com.automotive.email.entity.ServiceCompletionEmailRequestDTO;
import com.automotive.email.entity.SignupEmailRequestDTO;

import jakarta.validation.Valid;

public interface EmailService {

    void sendSignUpEmail(SignupEmailRequestDTO request);

	void sendServiceBookingEmail(@Valid ServiceBookingRequestDTO serviceBookingDTO);

	void sendServiceCompletionEmail(ServiceCompletionEmailRequestDTO dto);
}
