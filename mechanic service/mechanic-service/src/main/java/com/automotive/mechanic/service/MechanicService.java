package com.automotive.mechanic.service;

import com.automotive.mechanic.dto.MechanicWorkOrderResponse;
import com.automotive.mechanic.exception.WorkOrderException;

import java.util.List;

public interface MechanicService {
    List<MechanicWorkOrderResponse> listAssignedWorkOrders(Long mechanicId);
    MechanicWorkOrderResponse getWorkOrder(String serviceOrderId, Long mechanicId) throws WorkOrderException;
    MechanicWorkOrderResponse startWorkOrder(String serviceOrderId, Long mechanicId) throws WorkOrderException;
    MechanicWorkOrderResponse completeWorkOrder(String serviceOrderId, Long mechanicId, Double finalCost) throws WorkOrderException;
    MechanicWorkOrderResponse updateProgress(String serviceOrderId, Long mechanicId, String progress) throws WorkOrderException;
}
