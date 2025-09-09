package com.automotive.email.controller;

import com.automotive.email.entity.SignupEmailRequestDTO;
import com.automotive.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Email Service - Automotive Service Centre Management System",description="Automatic mail sender service")
@RestController
@RequestMapping("/api/email")
public class EmailServiceController {

    @Autowired
    private EmailService emailService;

    @Operation(summary = "Register a new user",
            description = "Registers a user (Customer, Service Manager, Admin) based on role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Duplicate email or phone number")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> sendSignUpEmail(@Valid @RequestBody SignupEmailRequestDTO signupDTO)
    {
        emailService.sendSignUpEmail(signupDTO);
        return ResponseEntity.ok("Signup email sent to " + signupDTO.getTo());
    }
}
