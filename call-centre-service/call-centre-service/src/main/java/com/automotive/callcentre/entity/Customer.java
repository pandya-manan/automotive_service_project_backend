package com.automotive.callcentre.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{

    @OneToMany(mappedBy="owner",cascade= CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    List<Vehicle> vehicles=new ArrayList<>();

    @JsonIgnore
    public List<Vehicle> getCustomerVehicles()
    {
        return this.vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setOwner(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setOwner(null);
    }


    @Override
    public String getType() {
        return "CUSTOMER";
    }
}
