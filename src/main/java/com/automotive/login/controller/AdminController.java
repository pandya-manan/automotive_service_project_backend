package com.automotive.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        logger.info("Accessing admin dashboard");
        return ResponseEntity.ok("Welcome to Admin Dashboard: Manage users, view reports, configure system");
    }
}