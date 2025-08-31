package com.automotive.signup.controller;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Tag(name="Sign Up Service -  Automotive Service Centre Management System",description="This API has the functionality for signing up new users onto the Automotive Service Platform")
@RestController
@RequestMapping("/signup")
@CrossOrigin(
        origins = "http://localhost:5173", // React app origin
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS},
        allowCredentials = "true"
)
public class SignUpServiceController {

    private static final Logger logger = Logger.getLogger(SignUpServiceController.class.getName());
    @Autowired
    private SignUpService signUpService;

    @Operation(summary = "Register a new user",
            description = "Registers a user (Customer, Service Manager, Admin) based on role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Duplicate email or phone number")
    })
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody SignUpRequestDTO dto) throws UserAlreadyExistsException {
        User savedUser = signUpService.registerUser(dto);
        logger.info("Obtained request body from the client as: "+dto.toString());
        return ResponseEntity.status(201).body(savedUser);
    }



}
