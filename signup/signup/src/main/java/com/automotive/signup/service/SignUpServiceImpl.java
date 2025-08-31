package com.automotive.signup.service;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.factory.UserFactory;
import com.automotive.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SignUpServiceImpl implements SignUpService{

    private static final Logger logger = Logger.getLogger(SignUpServiceImpl.class.getName());
    @Autowired
    private UserRepository userRepository;


    @Override
    public User registerUser(SignUpRequestDTO dto) throws UserAlreadyExistsException {
        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            logger.warning("Email already exists!");
            throw new UserAlreadyExistsException("Email already in use!");
        }
        if (userRepository.existsByUserPhoneNumber(dto.getUserPhoneNumber())) {
            logger.warning("Phone number already exists!");
            throw new UserAlreadyExistsException("Phone number already in use!");
        }

        User user = UserFactory.createUser(dto);
        logger.info("Saving to the database: "+user.toString());
        return userRepository.save(user);
    }
}
