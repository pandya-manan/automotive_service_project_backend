package com.automotive.login.security;

import com.automotive.login.entity.User;
import com.automotive.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.logging.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            logger.warning("User not found with email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        logger.info("Loaded user: " + user.getUserEmail() + " with role: " + user.getRole());

        String roleName = "ROLE_" + user.getRole().name();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPassword()) // must be encoded in DB
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(roleName)))
                .build();
    }
}
