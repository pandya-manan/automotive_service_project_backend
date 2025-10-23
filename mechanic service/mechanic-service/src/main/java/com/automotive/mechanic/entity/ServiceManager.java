package com.automotive.mechanic.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "assignedManager", cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Vehicle> assignedVehicles = new ArrayList<>();

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

    public void assignVehicle(Vehicle v) {
        assignedVehicles.add(v);
        v.setAssignedManager(this);
    }
    public void unassignVehicle(Vehicle v) {
        assignedVehicles.remove(v);
        v.setAssignedManager(null);
    }

    public List<Vehicle> getAssignedVehicles() {
        return assignedVehicles;
    }

    public void setAssignedVehicles(List<Vehicle> assignedVehicles) {
        this.assignedVehicles = assignedVehicles;
    }
}
