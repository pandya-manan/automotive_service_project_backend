package com.automotive.customer.service;

import com.automotive.customer.entity.WorkOrder;
import com.automotive.customer.entity.WorkOrderRequest;
import com.automotive.customer.entity.WorkOrderResponseDto;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;

public interface WorkOrderService {

    public WorkOrderResponseDto createWorkOrderForCustomer(Long customerId, Long vehicleId, WorkOrderRequest request) throws VehicleDoesNotExistException, CustomerDoesNotExistException;
}
