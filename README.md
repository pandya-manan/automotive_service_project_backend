# 🚗 Automotive Service Center Project  

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen?logo=springboot)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Eureka](https://img.shields.io/badge/Eureka-Server-blue?logo=spring)
![AWS](https://img.shields.io/badge/AWS-Cloud-ff9900?logo=amazonaws)
![React](https://img.shields.io/badge/React-18.2.0-61dafb?logo=react)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-f7df1e?logo=javascript)
![DaisyUI](https://img.shields.io/badge/DaisyUI-4.12.10-5a21f?logo=tailwindcss)
![Amazon S3](https://img.shields.io/badge/Amazon_S3-Storage-569a31?logo=amazons3)


A comprehensive microservices-based automotive service center platform built with Spring Boot and Spring Cloud, designed to streamline vehicle servicing operations from customer onboarding to work order completion.

## 🌟 Project Overview

This platform connects customers, call center agents, service managers, and mechanics through a seamless microservices architecture. The system handles everything from user registration and authentication to work order management and service notifications.

---
## 📡 Service Registry – Eureka Server

Eureka Server acts as the central service registry, enabling dynamic service discovery in our microservice architecture. It allows all services to register themselves and discover other services without hardcoded host/port configurations, ensuring high availability and load balancing.

---

## 🧩 Microservices

| Service | Description |
|---------|-------------|
| 📝 **Sign-Up Service** | Handles new user registration and onboarding to the automotive service center portal |
| 🔐 **Login Service** | Manages user authentication using Spring Security and JWT tokens for secure access |
| 👤 **Customer Service** | Handles all customer-related functionality and profile management |
| 📞 **Call Centre Service** | Enables call center agents to provide customer support and manage service requests |
| 📧 **Email Service** | Integrates with GMail SMTP to send notifications, status updates, and service reminders |
| 🔧 **Mechanic Service** | Allows mechanics to view assigned work orders, update status, and manage service tasks |
| 🏢 **Service Manager** | Provides interface for service managers to review new work orders and assign mechanics |
| 📋 **Service Registry** | Eureka-based registry service to manage registration and health status of all microservices |

---

## 🔄 Workflow

1. **Customer Journey**: Sign-up → Login → Create Service Request → Receive Notifications
2. **Service Management**: Work Order Creation → Manager Assignment → Mechanic Allocation → Service Completion
3. **Support System**: Customer Queries → Call Center Support → Issue Resolution

---

## 🏗️ Architecture & Tech Stack

- **Spring Boot 3.3.4** for microservices development
- **Spring Cloud** for distributed system patterns
- **Eureka Server** for service discovery and registration
- **Spring Security + JWT** for secure authentication
- **JUnit 5 + Mockito** for comprehensive unit testing
- **GMail SMTP** for email notifications

---

## 🛠️ Technical Highlights

### ✅ Core Features
- **Microservices Architecture** with Spring Boot 3.3.4
- **Service Discovery** via Eureka Server
- **Secure Authentication** with Spring Security + JWT
- **RESTful APIs** for inter-service communication
- **Centralized Configuration** management

### 🧪 Testing Strategy
- **Comprehensive Unit Testing** using JUnit 5 and Mockito across all services
- **Test-driven development** approach ensuring code quality
- **Mock dependencies** for isolated service testing
- **Integration tests** for critical service interactions

### 🔒 Security
- JWT-based stateless authentication
- Role-based access control
- Secure inter-service communication
- Password encryption and validation


**Built with ❤️ using Spring Boot and Microservices Architecture**
