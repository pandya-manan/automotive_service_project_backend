package com.automotive.servicemanager.controller;

import com.automotive.servicemanager.entity.Vehicle;
import com.automotive.servicemanager.entity.WorkOrder;
import com.automotive.servicemanager.exception.ServiceManagerDoesNotExistException;
import com.automotive.servicemanager.exception.VehicleDoesNotExistException;
import com.automotive.servicemanager.exception.WorkOrderDoesNotExistException;
import com.automotive.servicemanager.service.ServiceManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Service Manager Controller", description = "This API enables Service Managers to manage vehicles and work orders")
@RestController
@RequestMapping("/api/service-managers/{serviceManagerId}")
public class ServiceManagerController {

    @Autowired
    private ServiceManagerService serviceManagerService;

    @Operation(summary = "Get all vehicles assigned to a service manager", description = "Fetches all vehicles assigned to the service manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Service Manager does not exist")
    })
    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAssignedVehicles(@PathVariable Long serviceManagerId)
            throws ServiceManagerDoesNotExistException {
        List<Vehicle> vehicles = serviceManagerService.getAssignedVehicles(serviceManagerId);
        return ResponseEntity.ok(vehicles);
    }

    @Operation(summary = "Get all work orders assigned to a service manager", description = "Fetches all work orders managed by the service manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work orders fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Service Manager does not exist")
    })
    @GetMapping("/workorders")
    public ResponseEntity<List<WorkOrder>> getWorkOrders(@PathVariable Long serviceManagerId)
            throws ServiceManagerDoesNotExistException {
        List<WorkOrder> workOrders = serviceManagerService.getWorkOrdersForManager(serviceManagerId);
        return ResponseEntity.ok(workOrders);
    }

    public static class StatusUpdateRequest {
        public String status;
    }

    @Operation(summary = "Update work order status", description = "Updates the status of a work order (e.g., OPEN, IN_PROGRESS, COMPLETED, CANCELLED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Service Manager or Work Order"),
            @ApiResponse(responseCode = "400", description = "Invalid status provided")
    })
    @PutMapping("/workorders/{workOrderId}/status")
    public ResponseEntity<WorkOrder> updateWorkOrderStatus(@PathVariable Long serviceManagerId,
                                                           @PathVariable Long workOrderId,
                                                           @RequestBody String StatusUpdateRequest)
            throws ServiceManagerDoesNotExistException, WorkOrderDoesNotExistException {
        WorkOrder updatedWorkOrder = serviceManagerService.updateWorkOrderStatus(serviceManagerId, workOrderId, StatusUpdateRequest);
        return ResponseEntity.ok(updatedWorkOrder);
    }

    @Operation(summary = "Unassign a vehicle", description = "Removes a vehicle from the service manager's assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle unassigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Service Manager or Vehicle"),
            @ApiResponse(responseCode = "409", description = "Vehicle not assigned to this manager")
    })
    @DeleteMapping("/vehicles/{vehicleId}")
    public ResponseEntity<Void> unassignVehicle(@PathVariable Long serviceManagerId,
                                                @PathVariable Long vehicleId)
            throws ServiceManagerDoesNotExistException, VehicleDoesNotExistException {
        serviceManagerService.unassignVehicle(serviceManagerId, vehicleId);
        return ResponseEntity.noContent().build();
    }
}