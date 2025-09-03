package com.automotive.login.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value="SERVICE_MANAGER")
public class ServiceManager extends User{
    @Override
    public String getType() {
        return "SERVICE_MANAGER";
    }

    @Column(name="years_of_exp")
    private Integer yearsOfExperience;

    @Column(name="service_department")
    private String serviceDepartment;

    @OneToOne(mappedBy = "assignedManager", cascade = CascadeType.ALL)
    private Vehicle assignedVehicle;

    // --- getters and setters ---
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getServiceDepartment() {
        return serviceDepartment;
    }

    public void setServiceDepartment(String serviceDepartment) {
        this.serviceDepartment = serviceDepartment;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public void setAssignedVehicle(Vehicle assignedVehicle) {
        this.assignedVehicle = assignedVehicle;
        if (assignedVehicle != null) {
            assignedVehicle.setAssignedManager(this);
        }
    }

}
