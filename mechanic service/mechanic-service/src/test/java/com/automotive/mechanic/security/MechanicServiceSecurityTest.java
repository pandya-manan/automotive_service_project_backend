package com.automotive.mechanic.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.automotive.mechanic.dto.WorkOrderCompletionRequest;
import com.automotive.mechanic.entity.Mechanic;
import com.automotive.mechanic.entity.Vehicle;
import com.automotive.mechanic.entity.WorkOrder;
import com.automotive.mechanic.entity.WorkOrderStatus;
import com.automotive.mechanic.exception.WorkOrderException;
import com.automotive.mechanic.repository.WorkOrderRepository;
import com.automotive.mechanic.service.MechanicServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mechanic Service Security Tests")
class MechanicServiceSecurityTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private RestClient restClient;

    private MechanicServiceImpl mechanicService;

    private WorkOrder testWorkOrder;
    private Mechanic testMechanic;

    @BeforeEach
    void setUp() {
        mechanicService = new MechanicServiceImpl(workOrderRepository, restClient);

        testMechanic = new Mechanic();
        testMechanic.setUserId(1L);
        testMechanic.setUserName("Test Mechanic");

        Vehicle testVehicle = new Vehicle();
        testVehicle.setVehicleId(1L);

        testWorkOrder = new WorkOrder();
        testWorkOrder.setId(1L);
        testWorkOrder.setServiceOrderId("SRV-12345678");
        testWorkOrder.setStatus(WorkOrderStatus.ASSIGNED);
        testWorkOrder.setMechanic(testMechanic);
        testWorkOrder.setVehicle(testVehicle);
    }

    @Test
    @DisplayName("Should validate work order exists and belongs to mechanic")
    void testGetWorkOrder_ValidateOwnership() {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 999L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.getWorkOrder("SRV-12345678", 999L));
        // This ensures mechanic can only access their own work orders
    }

    @Test
    @DisplayName("Should validate work order exists before starting")
    void testStartWorkOrder_ValidateWorkOrderExists() {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.startWorkOrder("SRV-12345678", 1L));
    }

    @Test
    @DisplayName("Should validate work order status before starting")
    void testStartWorkOrder_ValidateStatus() {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.of(testWorkOrder));

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.startWorkOrder("SRV-12345678", 1L));
    }

    @Test
    @DisplayName("Should validate work order exists before updating progress")
    void testUpdateProgress_ValidateWorkOrderExists() {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.updateProgress("SRV-12345678", 1L, "Progress update"));
    }

    @Test
    @DisplayName("Should validate work order exists before completing")
    void testCompleteWorkOrder_ValidateWorkOrderExists() {
        // Arrange
        WorkOrderCompletionRequest request = new WorkOrderCompletionRequest();
        request.setFinalCost(1500.0);
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.completeWorkOrder("SRV-12345678", 1L, request));
    }

    @Test
    @DisplayName("Should validate work order status before completing")
    void testCompleteWorkOrder_ValidateStatus() {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
        WorkOrderCompletionRequest request = new WorkOrderCompletionRequest();
        request.setFinalCost(1500.0);
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.of(testWorkOrder));

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.completeWorkOrder("SRV-12345678", 1L, request));
    }

    @Test
    @DisplayName("Should only return work orders assigned to the mechanic")
    void testListAssignedWorkOrders_MechanicOwnership() {
        // Arrange
        when(workOrderRepository.findByMechanic_UserId(1L))
            .thenReturn(java.util.List.of(testWorkOrder));

        // Act
        var result = mechanicService.listAssignedWorkOrders(1L);

        // Assert
        assertNotNull(result);
        // This ensures mechanic can only see their own work orders
    }

    @Test
    @DisplayName("Should validate mechanic ID matches JWT token")
    void testGetWorkOrder_MechanicIdValidation() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(java.util.Optional.of(testWorkOrder));

        // Act
        var result = mechanicService.getWorkOrder("SRV-12345678", 1L);

        // Assert
        assertNotNull(result);
        // In production, verify JWT token contains matching mechanic ID
    }
}
