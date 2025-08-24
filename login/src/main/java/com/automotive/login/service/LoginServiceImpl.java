package com.automotive.login.service;

import com.automotive.login.dto.JwtResponseDTO;
import com.automotive.login.dto.LoginRequestDTO;
import com.automotive.login.dto.PasswordResetConfirmRequestDTO;
import com.automotive.login.dto.PasswordResetRequestDTO;
import com.automotive.login.entity.PasswordResetToken;
import com.automotive.login.repository.PasswordResetTokenRepository;
import com.automotive.login.security.JwtUtil;
import com.automotive.signup.entity.User;
import com.automotive.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public JwtResponseDTO login(LoginRequestDTO dto) {
        logger.info("Processing login request for: " + dto.getUserEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUserEmail(), dto.getUserPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByUserEmail(dto.getUserEmail());
            String token = jwtUtil.generateToken(user.getUserEmail(), user.getRole());
            String dashboardRedirect = switch (user.getRole()) {
                case CUSTOMER -> "/api/customer/dashboard";
                case SERVICE_MANAGER -> "/api/service-manager/dashboard";
                case ADMIN -> "/api/admin/dashboard";
            };
            JwtResponseDTO response = new JwtResponseDTO();
            response.setToken(token);
            response.setUserEmail(user.getUserEmail());
            response.setRole(user.getRole());
            response.setDashboardRedirect(dashboardRedirect);
            logger.info("Login successful for: " + dto.getUserEmail());
            return response;
        } catch (Exception e) {
            logger.warning("Login failed for: " + dto.getUserEmail() + " - " + e.getMessage());
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public String requestPasswordReset(PasswordResetRequestDTO dto) {
        logger.info("Processing password reset request for: " + dto.getUserEmail());
        User user = userRepository.findByUserEmail(dto.getUserEmail());
        if (user == null) {
            logger.warning("Email not found: " + dto.getUserEmail());
            throw new RuntimeException("Email not found");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);
        logger.info("Password reset token generated for: " + dto.getUserEmail());
        // In production, send email with reset link: e.g., http://localhost:3000/reset-password?token={token}
        return token; // Return for testing
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirmRequestDTO dto) {
        logger.info("Processing password reset confirmation");
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken());
        if (resetToken == null || resetToken.isExpired()) {
            logger.warning("Invalid or expired reset token");
            throw new RuntimeException("Invalid or expired reset token");
        }
        User user = resetToken.getUser();
        user.setUserPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
        logger.info("Password reset successful for: " + user.getUserEmail());
    }
}
