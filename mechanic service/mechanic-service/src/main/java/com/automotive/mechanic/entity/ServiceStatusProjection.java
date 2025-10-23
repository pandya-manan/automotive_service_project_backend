package com.automotive.mechanic.entity;

import java.time.LocalDateTime;

public interface ServiceStatusProjection {
    String getRegistrationNumber();
    Boolean getBookingStatus();
    Boolean getServiceCompleted();
    LocalDateTime getServiceBookingDate();
    String getServiceStatus();
    Long getServiceManager();
    Long getMechanicAssigned();
}
