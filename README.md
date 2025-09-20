🚗 Automotive Service Center Project

A microservices-based automotive service center platform built with Spring Boot and Spring Cloud.

📡 Service Registry – Eureka Server

Eureka Server acts as a service registry 📝 enabling service discovery in a microservice architecture.
It allows services to register themselves and discover other services dynamically, eliminating the need for hardcoded host/port configurations.

🧩 Microservices
Service	Description
📝 Sign-Up Service	Developed using Spring Boot to onboard new users to the automotive service center portal.
🔐 Login Service	Developed using Spring Boot to allow valid users to log in. Uses Spring Security and JWT tokens for authentication.
👤 Customer Service	Handles all functionality related to customers registered on our platform.
