package com.automotive.signup.service;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;

public interface SignUpService {

    User registerUser(SignUpRequestDTO dto) throws UserAlreadyExistsException;
}
