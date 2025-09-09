package com.automotive.signup.service;

import com.automotive.signup.dto.SignUpRequestDTO;
import com.automotive.signup.dto.SignupEmailRequestDTO;
import com.automotive.signup.entity.User;
import com.automotive.signup.exception.UserAlreadyExistsException;
import com.automotive.signup.factory.UserFactory;
import com.automotive.signup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.logging.Logger;

@Service
public class SignUpServiceImpl implements SignUpService {

    private static final Logger logger = Logger.getLogger(SignUpServiceImpl.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestClient restClient;

    @Value("${email-url}")
    private String EMAIL_SERVICE_URL;


//    private final String EMAIL_SERVICE_URL = "http://localhost:8080/api/email/signup";
    // Change this if email API runs on another port

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
        logger.info("Saving to the database: " + user.toString());
        User savedUser = userRepository.save(user);

        // Send Signup Email
        try {
            SignupEmailRequestDTO emailReq = new SignupEmailRequestDTO(
                    savedUser.getUserEmail(),
                    savedUser.getUserName(),
                    savedUser.getRole().name()
                     // frontend login link
            );

            restClient.post()
                    .uri(EMAIL_SERVICE_URL)
                    .body(emailReq)
                    .retrieve()
                    .toEntity(String.class);

            logger.info("Signup email sent successfully to: " + savedUser.getUserEmail());
        } catch (Exception e) {
            logger.warning("Failed to send signup email: " + e.getMessage());
            // Signup should succeed even if email fails
        }

        return savedUser;
    }
}
