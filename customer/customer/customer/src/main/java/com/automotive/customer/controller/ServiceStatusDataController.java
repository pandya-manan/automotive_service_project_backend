package com.automotive.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.automotive.customer.entity.ServiceStatusDTO;
import com.automotive.customer.entity.ServiceStatusProjection;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.service.WorkOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="Service Data Controller",description="This API will enable the customer to view the data related to service bookings and status")
@RestController
@CrossOrigin(
        origins = "http://localhost:5173", // React app origin
        allowedHeaders = "*",
        methods = {RequestMethod.POST,RequestMethod.GET},
        allowCredentials = "true"
)
@RequestMapping("/api/customers/{customerId}/service-status")
public class ServiceStatusDataController {

	@Autowired
    private WorkOrderService workOrderService;
	
	@Operation(summary="Fetch service data",description = "This API allows a customer to view service data")
    @ApiResponses(value={
            @ApiResponse(responseCode="200",description="Service Data available"),
            @ApiResponse(responseCode="409",description="Conflict"),
            @ApiResponse(responseCode="400",description="Bad Request")
    })
	 @GetMapping
	    public ResponseEntity<List<ServiceStatusProjection>> getServiceStatus(@PathVariable("customerId") Long customerId) throws VehicleDoesNotExistException, CustomerDoesNotExistException {
	        return ResponseEntity.ok(workOrderService.getServiceStatusForCustomer(customerId));
	    }
}
