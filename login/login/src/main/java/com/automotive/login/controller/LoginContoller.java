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
import com.automotive.login.entity.User;
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
@CrossOrigin(
        origins = "http://localhost:5173", // React app origin
        allowedHeaders = "*",
        methods = {RequestMethod.POST},
        allowCredentials = "true"
)
public class LoginContoller {
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping
	@Operation(summary = "Login a user",
    description = "Allows a user to login(Customer, Service Manager, Admin) based on role.")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login done successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Wrong password")
    })
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequestDTO loginRequest) throws UserNotFoundException, InvalidPasswordException {
        User user = loginService.login(loginRequest);
        return ResponseEntity.ok(user); // later you can return JWT or session token
    }

}
