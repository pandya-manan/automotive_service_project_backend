package com.automotive.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.automotive.customer.entity.WorkOrderRequest;
import com.automotive.customer.entity.WorkOrderResponseDto;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.service.WorkOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Work Order Controller",description="This API will enable the customer to book the vehicle for the service")
@RestController
@CrossOrigin(
        origins = "http://localhost:5173", // React app origin
        allowedHeaders = "*",
        methods = {RequestMethod.POST},
        allowCredentials = "true"
)
@RequestMapping("/api/customers/{customerId}/vehicles/{vehicleId}/workorders")
public class WorkOrderServiceController {

    @Autowired
    private WorkOrderService workOrderService;

    @Operation(summary="Book vehicle service",description = "This API allows a customer to book a vehicle for service")
    @ApiResponses(value={
            @ApiResponse(responseCode="201",description="Service Booked for the vehicle"),
            @ApiResponse(responseCode="400",description="Bad Request")
    })
    @PostMapping
    public ResponseEntity<WorkOrderResponseDto> bookVehicleForService(@PathVariable Long customerId, @PathVariable Long vehicleId, @RequestBody WorkOrderRequest workOrderRequest) throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        WorkOrderResponseDto workOrderResponseDto=workOrderService.createWorkOrderForCustomer(customerId, vehicleId, workOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(workOrderResponseDto);
    }
    
}
