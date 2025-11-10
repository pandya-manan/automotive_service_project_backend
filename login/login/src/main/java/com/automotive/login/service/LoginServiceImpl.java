package com.automotive.login.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.LoginResponseDTO;
import com.automotive.login.entity.User;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;
import com.automotive.login.repository.UserRepository;
import com.automotive.login.util.JwtTokenUtil;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.Valid;

@Service
public class LoginServiceImpl implements LoginService{
	
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	 @Override
	    public LoginResponseDTO login(@Valid LoginRequestDTO loginRequest) throws UserNotFoundException, InvalidPasswordException {
	        // Step 1: Find user by email
	        User obtainedUser = userRepo.findByUserEmail(loginRequest.getUserEmail());
	        if (obtainedUser == null) {
	            logger.warning("No user found with email: " + loginRequest.getUserEmail());
	            throw new UserNotFoundException("No user found with email: " + loginRequest.getUserEmail());
	        }

	        // Step 2: Validate password using BCrypt
	        if (!passwordEncoder.matches(loginRequest.getUserPassword(), obtainedUser.getUserPassword())) {
	            logger.warning("Wrong password entered for user email: " + loginRequest.getUserEmail());
	            throw new InvalidPasswordException("Wrong password entered. Please try again.");
	        }

	        // Step 3: Generate JWT token
	        String token = jwtTokenUtil.generateToken(obtainedUser);
	        
	        // Step 4: Create and return login response with JWT token
	        logger.info("Login successful for user: " + loginRequest.getUserEmail() + " with role: " + obtainedUser.getRole());
	        return new LoginResponseDTO(token, obtainedUser);
	    }
}

