package com.automotive.service.controller;

import com.automotive.service.dto.*;
import com.automotive.service.exception.WorkOrderException;
import com.automotive.service.serviceimpl.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ServiceManagerController {

    private final ServiceManagerService service;

    public ServiceManagerController(ServiceManagerService service) {
        this.service = service;
    }

    @GetMapping("/workorders/open")
    public ResponseEntity<List<WorkOrderResponseDto>> listOpen() {
        return ResponseEntity.ok(service.listOpenWorkOrders());
    }

    @GetMapping("/workorders/{serviceOrderId}")
    public ResponseEntity<WorkOrderResponseDto> get(@PathVariable String serviceOrderId) throws WorkOrderException {
        return ResponseEntity.ok(service.getByServiceOrderId(serviceOrderId));
    }

    @PostMapping("/workorders/{serviceOrderId}/assign-manager")
    public ResponseEntity<WorkOrderResponseDto> assignManager(@PathVariable String serviceOrderId,
                                                              @Valid @RequestBody AssignManagerRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.assignManager(serviceOrderId, req));
    }

    @PostMapping("/workorders/{serviceOrderId}/assign-mechanic")
    public ResponseEntity<WorkOrderResponseDto> assignMechanic(@PathVariable String serviceOrderId,
                                                               @Valid @RequestBody AssignMechanicRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.assignMechanic(serviceOrderId, req));
    }

    @PostMapping("/workorders/{serviceOrderId}/start")
    public ResponseEntity<WorkOrderResponseDto> startWorkOrder(@PathVariable String serviceOrderId,
                                                               @Valid @RequestBody SimpleIdRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.startWorkOrder(serviceOrderId, req));
    }

    @PostMapping("/workorders/{serviceOrderId}/complete")
    public ResponseEntity<WorkOrderResponseDto> completeWorkOrder(@PathVariable String serviceOrderId,
                                                                  @Valid @RequestBody UpdateCostsRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.completeWorkOrder(serviceOrderId, req));
    }

    @PatchMapping("/workorders/{serviceOrderId}/costs")
    public ResponseEntity<WorkOrderResponseDto> updateCosts(@PathVariable String serviceOrderId,
                                                            @RequestBody UpdateCostsRequest req) throws WorkOrderException {
        return ResponseEntity.ok(service.updateCosts(serviceOrderId, req));
    }
}
