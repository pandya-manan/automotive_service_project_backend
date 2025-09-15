package com.automotive.customer.entity;

import com.automotive.customer.entity.AvailabilityStatus;
import com.automotive.customer.entity.Specialization;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MECHANIC")
public class Mechanic extends User {

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Specialization specialization;

    @Column(name = "years_of_exp")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;

    private Double hourlyRate;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<WorkOrder> workOrders = new ArrayList<>();

    // convenience
    public void addWorkOrder(WorkOrder w) {
        workOrders.add(w);
        w.setMechanic(this);
    }

    @Override
    public String getType() {
        return "MECHANIC";
    }

    // getters/setters...


    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }


    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }

    @Override
    public String toString() {
        return "Mechanic{" +
                ", specialization=" + specialization +
                ", yearsOfExperience=" + yearsOfExperience +
                ", availabilityStatus=" + availabilityStatus +
                ", hourlyRate=" + hourlyRate +
                ", workOrders=" + workOrders +
                '}';
    }
}
