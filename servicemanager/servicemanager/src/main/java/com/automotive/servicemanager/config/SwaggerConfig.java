package com.automotive.servicemanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class SwaggerConfig {

    private static final Logger logger = Logger.getLogger(SwaggerConfig.class.getName());

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Initializing OpenAPI configuration for Service Manager API");
        return new OpenAPI()
                .info(new Info()
                        .title("Service Manager API")
                        .description("API for managing vehicles and work orders for service managers in the Automotive Service Center Management System")
                        .version("1.0.0"));
    }
}