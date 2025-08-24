package com.automotive.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {
    private static final Logger logger = Logger.getLogger(CustomerController.class.getName());

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        logger.info("Accessing customer dashboard");
        return ResponseEntity.ok("Welcome to Customer Dashboard: Book service, view history, track vehicle status");
    }
}
