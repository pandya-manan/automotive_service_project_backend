package com.automotive.login.service;

import com.automotive.login.entity.LoginRequestDTO;
import com.automotive.login.entity.LoginResponseDTO;
import com.automotive.login.exception.InvalidPasswordException;
import com.automotive.login.exception.UserNotFoundException;

import jakarta.validation.Valid;

public interface LoginService {

	LoginResponseDTO login(@Valid LoginRequestDTO loginRequest) throws UserNotFoundException, InvalidPasswordException;

}
