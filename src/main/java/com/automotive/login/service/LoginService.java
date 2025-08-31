package com.automotive.login.service;

import com.automotive.login.dto.JwtResponseDTO;
import com.automotive.login.dto.LoginRequestDTO;
import com.automotive.login.dto.PasswordResetConfirmRequestDTO;
import com.automotive.login.dto.PasswordResetRequestDTO;

public interface LoginService {
    JwtResponseDTO login(LoginRequestDTO dto);
    String requestPasswordReset(PasswordResetRequestDTO dto);
    void confirmPasswordReset(PasswordResetConfirmRequestDTO dto);
}