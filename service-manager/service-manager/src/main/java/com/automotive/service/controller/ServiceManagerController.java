package com.automotive.service.controller;

import com.automotive.service.dto.*;
import com.automotive.service.exception.WorkOrderException;
import com.automotive.service.serviceimpl.ServiceManagerService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Service Manager - Automotive Service Center", description = "APIs for service manager to manage work orders, assign self, assign mechanics, and handle costs")
@RestController
@RequestMapping("/api/manager")
public class ServiceManagerController {

    private final ServiceManagerService service;

    public ServiceManagerController(ServiceManagerService service) {
        this.service = service;
    }

    @Operation(summary = "List all open work orders", description = "Fetch all work orders that have status OPEN and are waiting for assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of open work orders fetched successfully")
    })
    @GetMapping("/workorders/open")
    public ResponseEntity<List<WorkOrderResponseDto>> listOpen() {
        return ResponseEntity.ok(service.listOpenWorkOrders());
    }

    @Operation(summary = "Get work order details", description = "Fetch details of a single work order by service order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work order fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Work order not found")
    })
    @GetMapping("/workorders/{serviceOrderId}")
    public ResponseEntity<WorkOrderResponseDto> get(@PathVariable String serviceOrderId) throws WorkOrderException {
        return ResponseEntity.ok(service.getByServiceOrderId(serviceOrderId));
    }

    @Operation(summary = "Assign service manager to work order", description = "Assign yourself as the service manager for a work order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Work order not found")
    })
    @PostMapping("/workorders/{serviceOrderId}/assign-manager")
    public ResponseEntity<WorkOrderResponseDto> assignManager(@PathVariable String serviceOrderId,
                                                              @Valid @RequestBody AssignManagerRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.assignManager(serviceOrderId, req));
    }

    @Operation(summary = "Assign mechanic and set estimated cost", description = "Assign a mechanic to a work order and optionally set the estimated cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mechanic assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Work order or mechanic not found")
    })
    @PostMapping("/workorders/{serviceOrderId}/assign-mechanic")
    public ResponseEntity<WorkOrderResponseDto> assignMechanic(@PathVariable String serviceOrderId,
                                                               @Valid @RequestBody AssignMechanicRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.assignMechanic(serviceOrderId, req));
    }

    @Operation(summary = "Start a work order", description = "Mark the work order as IN_PROGRESS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work order started successfully"),
            @ApiResponse(responseCode = "404", description = "Work order not found")
    })
    @PostMapping("/workorders/{serviceOrderId}/start")
    public ResponseEntity<WorkOrderResponseDto> startWorkOrder(@PathVariable String serviceOrderId,
                                                               @Valid @RequestBody SimpleIdRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.startWorkOrder(serviceOrderId, req));
    }

    @Operation(summary = "Complete a work order", description = "Mark the work order as COMPLETED and set the final cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work order completed successfully"),
            @ApiResponse(responseCode = "404", description = "Work order not found")
    })
    @PostMapping("/workorders/{serviceOrderId}/complete")
    public ResponseEntity<WorkOrderResponseDto> completeWorkOrder(@PathVariable String serviceOrderId,
                                                                  @Valid @RequestBody UpdateCostsRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.completeWorkOrder(serviceOrderId, req));
    }

    @Operation(summary = "Update costs of a work order", description = "Update estimated or final costs of a work order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Work order costs updated successfully"),
            @ApiResponse(responseCode = "404", description = "Work order not found")
    })
    @PatchMapping("/workorders/{serviceOrderId}/costs")
    public ResponseEntity<WorkOrderResponseDto> updateCosts(@PathVariable String serviceOrderId,
                                                            @RequestBody UpdateCostsRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.updateCosts(serviceOrderId, req));
    }
}
