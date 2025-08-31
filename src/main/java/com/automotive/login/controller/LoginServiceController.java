package com.automotive.login.controller;

import com.automotive.login.dto.JwtResponseDTO;
import com.automotive.login.dto.LoginRequestDTO;
import com.automotive.login.dto.PasswordResetConfirmRequestDTO;
import com.automotive.login.dto.PasswordResetRequestDTO;
import com.automotive.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/login")
@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS},
        allowCredentials = "true"
)
public class LoginServiceController {
    private static final Logger logger = Logger.getLogger(LoginServiceController.class.getName());

    @Autowired
    private LoginService loginService;

    @Operation(summary = "Log in a user",
            description = "Authenticates a user (Customer, Service Manager, Admin) using email and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        logger.info("Received login request: " + dto.toString());
        JwtResponseDTO response = loginService.login(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Request password reset",
            description = "Generates a password reset token for the provided email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset token generated"),
            @ApiResponse(responseCode = "400", description = "Invalid email")
    })
    @PostMapping("/password/reset/request")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO dto) {
        logger.info("Received password reset request: " + dto.toString());
        String token = loginService.requestPasswordReset(dto);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Confirm password reset",
            description = "Resets the user password using a valid reset token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset token")
    })
    @PostMapping("/password/reset/confirm")
    public ResponseEntity<String> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmRequestDTO dto) {
        logger.info("Received password reset confirmation request");
        loginService.confirmPasswordReset(dto);
        return ResponseEntity.ok("Password reset successful");
    }

    @Operation(summary = "Log out a user",
            description = "Invalidates the user session (frontend clears JWT token).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        logger.info("Received logout request");
        return ResponseEntity.ok("Logged out successfully");
    }
}