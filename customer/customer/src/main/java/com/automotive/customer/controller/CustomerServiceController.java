package com.automotive.customer.controller;

import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name="Customer Service - Automotive Service Center Management System",description="This API has the functionality for allowing customers to perform crud on vehicles")
@RestController
@RequestMapping("/api/customers/{customerId}/vehicles")
@CrossOrigin(
        origins = "http://localhost:5173", // React app origin
        allowedHeaders = "*",
        methods = {RequestMethod.POST},
        allowCredentials = "true"
)
public class CustomerServiceController {

    @Autowired
    private VehicleService vehicleService;

    @Operation(summary = "Add a new vehicle",
            description = "This API allows a customer to add a new vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle added successfully")
    })
    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@PathVariable Long customerId,
                                              @RequestBody Vehicle vehicle)
            throws CustomerDoesNotExistException {
        Vehicle saved = vehicleService.addVehicleToCustomer(customerId, vehicle);

        // Return 201 Created + Location header
        URI location = URI.create(String.format("/api/customers/%d/vehicles/%d",
                customerId, saved.getVehicleId()));
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(summary = "Get all vehicles for a customer",
            description = "Fetches all vehicles belonging to a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<Vehicle>> getVehicles(@PathVariable Long customerId)
            throws CustomerDoesNotExistException {
        List<Vehicle> vehicles = vehicleService.getAllVehiclesForCustomer(customerId);
        return ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Update an existing vehicle",
            description = "Allows a customer to update a vehicle by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully")
    })
    @PutMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long customerId,
                                                 @PathVariable Long vehicleId,
                                                 @RequestBody Vehicle updatedVehicle)
            throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        Vehicle updated = vehicleService.updateVehicle(vehicleId, updatedVehicle);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a vehicle",
            description = "Allows a customer to delete a vehicle by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully")
    })
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long customerId,
                                              @PathVariable Long vehicleId)
            throws VehicleDoesNotExistException {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
