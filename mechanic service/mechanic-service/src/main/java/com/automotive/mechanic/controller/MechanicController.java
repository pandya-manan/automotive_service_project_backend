package com.automotive.mechanic.controller;

import com.automotive.mechanic.dto.MechanicWorkOrderResponse;
import com.automotive.mechanic.exception.WorkOrderException;
import com.automotive.mechanic.service.MechanicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Mechanic - Automotive Service Center", description="APIs for mechanics to manage their assigned work orders")
@RestController
@RequestMapping("/api/mechanic")
public class MechanicController {

    private final MechanicService service;

    public MechanicController(MechanicService service) {
        this.service = service;
    }

    @Operation(summary="List assigned work orders", description="Fetch all work orders assigned to a mechanic")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="List fetched successfully")
    })
    @GetMapping("/{mechanicId}/workorders")
    public ResponseEntity<List<MechanicWorkOrderResponse>> listAssigned(@PathVariable Long mechanicId) {
        return ResponseEntity.ok(service.listAssignedWorkOrders(mechanicId));
    }

    @Operation(summary="Get work order details", description="Fetch details of a work order assigned to the mechanic")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Work order fetched successfully"),
        @ApiResponse(responseCode="404", description="Work order not found")
    })
    @GetMapping("/{mechanicId}/workorders/{serviceOrderId}")
    public ResponseEntity<MechanicWorkOrderResponse> get(@PathVariable Long mechanicId,
                                                         @PathVariable String serviceOrderId) throws WorkOrderException {
        return ResponseEntity.ok(service.getWorkOrder(serviceOrderId, mechanicId));
    }

    @Operation(summary="Start work order", description="Mark a work order as IN_PROGRESS")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Work order started successfully"),
        @ApiResponse(responseCode="400", description="Invalid status transition")
    })
    @PostMapping("/{mechanicId}/workorders/{serviceOrderId}/start")
    public ResponseEntity<MechanicWorkOrderResponse> start(@PathVariable Long mechanicId,
                                                           @PathVariable String serviceOrderId) throws WorkOrderException {
        return ResponseEntity.ok(service.startWorkOrder(serviceOrderId, mechanicId));
    }

    @Operation(summary="Complete work order", description="Mark a work order as COMPLETED and update final cost")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Work order completed successfully"),
        @ApiResponse(responseCode="400", description="Invalid status transition")
    })
    @PostMapping("/{mechanicId}/workorders/{serviceOrderId}/complete")
    public ResponseEntity<MechanicWorkOrderResponse> complete(@PathVariable Long mechanicId,
                                                              @PathVariable String serviceOrderId,
                                                              @RequestParam Double finalCost) throws WorkOrderException {
        return ResponseEntity.ok(service.completeWorkOrder(serviceOrderId, mechanicId, finalCost));
    }

    @Operation(summary="Update work order progress", description="Update progress or status message for a work order")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Progress updated successfully"),
        @ApiResponse(responseCode="404", description="Work order not found")
    })
    @PatchMapping("/{mechanicId}/workorders/{serviceOrderId}/progress")
    public ResponseEntity<MechanicWorkOrderResponse> updateProgress(@PathVariable Long mechanicId,
                                                                    @PathVariable String serviceOrderId,
                                                                    @RequestParam String progress) throws WorkOrderException {
        return ResponseEntity.ok(service.updateProgress(serviceOrderId, mechanicId, progress));
    }
}
