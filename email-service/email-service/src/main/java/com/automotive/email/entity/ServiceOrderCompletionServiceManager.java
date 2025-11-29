package com.automotive.email.entity;
import java.time.*;
public class ServiceOrderCompletionServiceManager {


	    private String to;
	    private String from;
	    private String serviceOrderId;
	    private String vehicleVin;
	    private String make;
	    private String model;
	    private String serviceManager;
	    private String mechanic;
	    private double estimatedCost;
	    private double finalCost;
	    private OffsetDateTime completedAt;

	    private String userName;

	    // Getters and Setters
	    public String getTo() { return to; }
	    public void setTo(String to) { this.to = to; }

	    public String getFrom() { return from; }
	    public void setFrom(String from) { this.from = from; }

	    public String getServiceOrderId() { return serviceOrderId; }
	    public void setServiceOrderId(String serviceOrderId) { this.serviceOrderId = serviceOrderId; }

	    public String getVehicleVin() { return vehicleVin; }
	    public void setVehicleVin(String vehicleVin) { this.vehicleVin = vehicleVin; }

	    public String getMake() { return make; }
	    public void setMake(String make) { this.make = make; }

	    public String getModel() { return model; }
	    public void setModel(String model) { this.model = model; }

	    public String getServiceManager() { return serviceManager; }
	    public void setServiceManager(String serviceManager) { this.serviceManager = serviceManager; }

	    public String getMechanic() { return mechanic; }
	    public void setMechanic(String mechanic) { this.mechanic = mechanic; }

	    public double getEstimatedCost() { return estimatedCost; }
	    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }

	    public double getFinalCost() { return finalCost; }
	    public void setFinalCost(double finalCost) { this.finalCost = finalCost; }

	    public OffsetDateTime getCompletedAt() { return completedAt; }
	    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }

	    public String getUserName() { return userName; }
	    public void setUserName(String userName) { this.userName = userName; }
	

}
