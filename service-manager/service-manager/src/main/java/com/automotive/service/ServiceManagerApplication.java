package com.automotive.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.automotive.service",
})
public class ServiceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceManagerApplication.class, args);
	}

}
