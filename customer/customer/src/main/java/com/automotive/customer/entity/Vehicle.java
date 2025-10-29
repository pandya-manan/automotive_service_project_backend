package com.automotive.customer.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(name = "vehicles",
        indexes = {
                @Index(name = "idx_vehicle_vin", columnList = "vin"),
                @Index(name = "idx_vehicle_reg", columnList = "registration_number")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_vehicle_vin", columnNames = {"vin"}),
                @UniqueConstraint(name = "uc_vehicle_reg", columnNames = {"registration_number"})
        }
)
public class Vehicle {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(length = 50, nullable = false)
    private String vin;

    @Column(length = 100)
    private String make;

    @Column(length = 100)
    private String model;

    @Column(name = "registration_number", length = 50, nullable = false)
    private String registrationNumber;

    @Column(name = "is_insured")
    private Boolean isInsured;

    @Column(name = "year_of_registration")
    private Integer yearOfRegistration;

    @Column(name = "is_booked_for_service")
    private Boolean isBookedForService;

    @Column(name = "is_service_done")
    private Boolean isServiceDone;

    @Version
    private Long version;   //Optimistic Locking Feature
    
    @Column(name="vehicle_image_url")
    private String vehicleImageUrl;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private Customer owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_manager_id")
    private ServiceManager assignedManager;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkOrder> workOrders = new ArrayList<>();

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt;
    
    

    public Boolean getIsInsured() {
		return isInsured;
	}
	public void setIsInsured(Boolean isInsured) {
		this.isInsured = isInsured;
	}
	public Boolean getIsBookedForService() {
		return isBookedForService;
	}
	public void setIsBookedForService(Boolean isBookedForService) {
		this.isBookedForService = isBookedForService;
	}
	public Boolean getIsServiceDone() {
		return isServiceDone;
	}
	public void setIsServiceDone(Boolean isServiceDone) {
		this.isServiceDone = isServiceDone;
	}
	public String getVehicleImageUrl() {
		return vehicleImageUrl;
	}
	public void setVehicleImageUrl(String vehicleImageUrl) {
		this.vehicleImageUrl = vehicleImageUrl;
	}
	// Convenience
    public void addWorkOrder(WorkOrder wo) {
        workOrders.add(wo);
        wo.setVehicle(this);
    }
    public void removeWorkOrder(WorkOrder wo) {
        workOrders.remove(wo);
        wo.setVehicle(null);
    }

    // getter/setter
    public ServiceManager getAssignedManager() {
        return assignedManager;
    }
    public void setAssignedManager(ServiceManager assignedManager) {
        this.assignedManager = assignedManager;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Boolean getInsured() {
        return isInsured;
    }

    public void setInsured(Boolean insured) {
        isInsured = insured;
    }

    public Integer getYearOfRegistration() {
        return yearOfRegistration;
    }

    public void setYearOfRegistration(Integer yearOfRegistration) {
        this.yearOfRegistration = yearOfRegistration;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public Boolean getBookedForService() {
        return isBookedForService;
    }

    public void setBookedForService(Boolean bookedForService) {
        isBookedForService = bookedForService;
    }

    public Boolean getServiceDone() {
        return isServiceDone;
    }

    public void setServiceDone(Boolean serviceDone) {
        isServiceDone = serviceDone;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vin='" + vin + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", isInsured=" + isInsured +
                ", yearOfRegistration=" + yearOfRegistration +
                ", isBookedForService=" + isBookedForService +
                ", isServiceDone=" + isServiceDone +
                ", version=" + version +
                ", owner=" + owner +
                ", assignedManager=" + assignedManager +
                ", workOrders=" + workOrders +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(List<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }
}
