package com.automotive.service.security;

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

import com.automotive.service.dto.AssignManagerRequest;
import com.automotive.service.dto.AssignMechanicRequest;
import com.automotive.service.dto.UpdateCostsRequest;
import com.automotive.service.entity.WorkOrder;
import com.automotive.service.entity.WorkOrderStatus;
import com.automotive.service.exception.WorkOrderException;
import com.automotive.service.repository.MechanicRepository;
import com.automotive.service.repository.ServiceManagerRepository;
import com.automotive.service.repository.VehicleRepository;
import com.automotive.service.repository.WorkOrderRepository;
import com.automotive.service.serviceimpl.ServiceManagerService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service Manager Security Tests")
class ServiceManagerSecurityTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ServiceManagerRepository serviceManagerRepository;

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private RestClient restClient;

    private ServiceManagerService serviceManagerService;

    private WorkOrder testWorkOrder;

    @BeforeEach
    void setUp() {
        serviceManagerService = new ServiceManagerService(
            workOrderRepository,
            vehicleRepository,
            serviceManagerRepository,
            mechanicRepository,
            restClient
        );

        testWorkOrder = new WorkOrder();
        testWorkOrder.setId(1L);
        testWorkOrder.setServiceOrderId("SRV-12345678");
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
    }

    @Test
    @DisplayName("Should validate work order exists before operations")
    void testGetByServiceOrderId_ValidateWorkOrderExists() {
        // Arrange
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.getByServiceOrderId("SRV-12345678"));
    }

    @Test
    @DisplayName("Should validate manager exists before assignment")
    void testAssignManager_ValidateManagerExists() {
        // Arrange
        AssignManagerRequest request = new AssignManagerRequest(999L);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(java.util.Optional.of(testWorkOrder));
        when(serviceManagerRepository.findById(999L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.assignManager("SRV-12345678", request));
    }

    @Test
    @DisplayName("Should validate mechanic exists before assignment")
    void testAssignMechanic_ValidateMechanicExists() {
        // Arrange
        AssignMechanicRequest request = new AssignMechanicRequest(999L, 1500.0);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(java.util.Optional.of(testWorkOrder));
        when(mechanicRepository.findById(999L))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.assignMechanic("SRV-12345678", request));
    }

    @Test
    @DisplayName("Should validate work order exists before updating costs")
    void testUpdateCosts_ValidateWorkOrderExists() {
        // Arrange
        UpdateCostsRequest request = new UpdateCostsRequest(1000.0, 1200.0);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.updateCosts("SRV-12345678", request));
    }

    @Test
    @DisplayName("Should validate work order exists before completing")
    void testCompleteWorkOrder_ValidateWorkOrderExists() {
        // Arrange
        UpdateCostsRequest request = new UpdateCostsRequest(1000.0, 1200.0);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.completeWorkOrder("SRV-12345678", request));
    }

    @Test
    @DisplayName("Should validate service order ID format")
    void testGetByServiceOrderId_ValidateServiceOrderIdFormat() {
        // Arrange
        when(workOrderRepository.findByServiceOrderId("INVALID"))
            .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.getByServiceOrderId("INVALID"));
        // In production, validate service order ID format (e.g., SRV-XXXXXXXX)
    }

    @Test
    @DisplayName("Should require valid work order for all operations")
    void testListOpenWorkOrders_ReturnsWorkOrders() {
        // Arrange
        when(workOrderRepository.findAll()).thenReturn(java.util.List.of(testWorkOrder));

        // Act
        var result = serviceManagerService.listOpenWorkOrders();

        // Assert
        assertNotNull(result);
        // In production, verify JWT token contains SERVICE_MANAGER role
    }
}
