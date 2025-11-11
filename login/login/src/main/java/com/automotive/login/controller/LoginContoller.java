package com.automotive.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.LoginResponseDTO;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;
import com.automotive.login.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name="Login Service - Automotive Service Center Management System",description="This API has the functionality for allowing existing users to login to the platform")
@RestController
@RequestMapping("/login")
public class LoginContoller {
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping
	@Operation(summary = "Login a user",
    description = "Allows a user to login(Customer, Service Manager, Admin, Call Centre Agent, Mechanic) based on role. Returns JWT token for authentication.")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login done successfully, returns JWT token"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Wrong password")
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) throws UserNotFoundException, InvalidPasswordException {
        LoginResponseDTO loginResponse = loginService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

}
