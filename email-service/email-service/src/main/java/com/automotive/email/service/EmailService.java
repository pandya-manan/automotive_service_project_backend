package com.automotive.email.service;

import com.automotive.email.entity.SignupEmailRequestDTO;

public interface EmailService {

    void sendSignUpEmail(SignupEmailRequestDTO request);
}
