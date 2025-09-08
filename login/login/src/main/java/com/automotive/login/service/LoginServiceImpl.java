package com.automotive.login.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.User;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;
import com.automotive.login.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class LoginServiceImpl implements LoginService{
	
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());
	
	@Autowired
	private UserRepository userRepo;

	 @Override
	    public User login(@Valid LoginRequestDTO loginRequest) throws UserNotFoundException, InvalidPasswordException {
	        // Step 1: Find user by email
	        User obtainedUser = userRepo.findByUserEmail(loginRequest.getUserEmail());
	        if (obtainedUser == null) {
	            logger.warning("No user found with email: " + loginRequest.getUserEmail());
	            throw new UserNotFoundException("No user found with email: " + loginRequest.getUserEmail());
	        }

	        // Step 2: Validate password
	        if (!loginRequest.getUserPassword().equals(obtainedUser.getUserPassword())) {
	            logger.warning("Wrong password entered for user email: " + loginRequest.getUserEmail());
	            throw new InvalidPasswordException("Wrong password entered. Please try again.");
	        }

	        // Step 3: Success
	        logger.info("Login successful for user: " + loginRequest.getUserEmail());
	        return obtainedUser;
	    }
}

