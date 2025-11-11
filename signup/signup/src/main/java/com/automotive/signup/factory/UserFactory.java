package com.automotive.signup.factory;
import com.automotive.signup.entity.*;
import com.automotive.signup.dto.SignUpRequestDTO;

import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserFactory {

    private static final Logger logger = Logger.getLogger(UserFactory.class.getName());
    public static User createUser(SignUpRequestDTO dto) {
        Role role = Role.valueOf(dto.getRole().toUpperCase());
        logger.info("Obtained Role From the database is: "+role.name());

        switch (role) {
            case CUSTOMER -> {
                Customer customer = new Customer();
                logger.info("Customer object being created for username: "+dto.getUserName());
                populateCommonFields(customer, dto, role);
                return customer;
            }
            case SERVICE_MANAGER -> {
                ServiceManager manager = new ServiceManager();
                logger.info("Service Manager object being created for username: "+dto.getUserName());
                populateCommonFields(manager, dto, role);
                // Later you can accept yearsOfExperience/department in DTO
                manager.setYearsOfExperience(dto.getYearsOfExperience());
                manager.setServiceDepartment(dto.getServiceDepartment());
                return manager;
            }
            case ADMIN -> {
                AdminUser admin = new AdminUser(); // assume you’ll create this class
                logger.info("Admin user being created for the username: " + dto.getUserName());
                populateCommonFields(admin, dto, role);
                return admin;
            }
            case MECHANIC -> {
                Mechanic mechanic = new Mechanic();
                logger.info("Mechanic user being created for the username: " + dto.getUserName());
                populateCommonFields(mechanic, dto, role);

                mechanic.setHourlyRate(dto.getHourlyRate());
                mechanic.setSpecialization(
                        Specialization.valueOf(dto.getSpecialization().toUpperCase(Locale.ROOT))
                );
                mechanic.setYearsOfExperience(dto.getYearsOfExperience());
                mechanic.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

                return mechanic;
            }
            case CALL_CENTRE_AGENT->
            {
                CallCenterAgent callCentreAgent=new CallCenterAgent();
                logger.info("Call Center Agent user being created for the username: "+dto.getUserName());
                populateCommonFields(callCentreAgent,dto,role);
                return callCentreAgent;
            }

            default -> throw new IllegalArgumentException("Unsupported role: " + dto.getRole());

        }


}

    private static void populateCommonFields(User user, SignUpRequestDTO dto, Role role) {
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(dto.getUserPassword()); // Password will be encrypted in service layer
        user.setUserPhoneNumber(dto.getUserPhoneNumber());
        user.setUserAddress(dto.getUserAddress());
        user.setRole(role);
        user.setUserImageUrl(dto.getUserImageUrl());
        logger.info(user.toString()+" User object populated using common fields");
    }


}
