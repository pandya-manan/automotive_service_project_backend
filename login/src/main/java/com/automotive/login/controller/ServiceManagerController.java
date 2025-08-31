package com.automotive.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/service-manager")
@PreAuthorize("hasRole('SERVICE_MANAGER')")
public class ServiceManagerController {
    private static final Logger logger = Logger.getLogger(ServiceManagerController.class.getName());

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        logger.info("Accessing service manager dashboard");
        return ResponseEntity.ok("Welcome to Service Manager Dashboard: Manage bookings, update status, view customer details");
    }
}
