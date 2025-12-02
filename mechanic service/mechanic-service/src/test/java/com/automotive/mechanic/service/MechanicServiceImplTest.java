package com.automotive.mechanic.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.automotive.mechanic.dto.MechanicWorkOrderResponse;
import com.automotive.mechanic.entity.Mechanic;
import com.automotive.mechanic.entity.Vehicle;
import com.automotive.mechanic.entity.WorkOrder;
import com.automotive.mechanic.entity.WorkOrderStatus;
import com.automotive.mechanic.exception.WorkOrderException;
import com.automotive.mechanic.repository.WorkOrderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MechanicServiceImpl Tests")
class MechanicServiceImplTest {

    @Mock
    private WorkOrderRepository workOrderRepository;

    private MechanicServiceImpl mechanicService;

    private WorkOrder testWorkOrder;
    private Mechanic testMechanic;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        mechanicService = new MechanicServiceImpl(workOrderRepository, null);

        testMechanic = new Mechanic();
        testMechanic.setUserId(1L);
        testMechanic.setUserName("Test Mechanic");

        testVehicle = new Vehicle();
        testVehicle.setVehicleId(1L);
        testVehicle.setVin("VIN123");

        testWorkOrder = new WorkOrder();
        testWorkOrder.setId(1L);
        testWorkOrder.setServiceOrderId("SRV-12345678");
        testWorkOrder.setStatus(WorkOrderStatus.ASSIGNED);
        testWorkOrder.setMechanic(testMechanic);
        testWorkOrder.setVehicle(testVehicle);
        testWorkOrder.setDescription("Test service");
    }

    @Test
    @DisplayName("Should successfully list assigned work orders")
    void testListAssignedWorkOrders_Success() {
        // Arrange
        List<WorkOrder> workOrders = new ArrayList<>();
        workOrders.add(testWorkOrder);
        when(workOrderRepository.findByMechanic_UserId(1L)).thenReturn(workOrders);

        // Act
        List<MechanicWorkOrderResponse> result = mechanicService.listAssignedWorkOrders(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SRV-12345678", result.get(0).getServiceOrderId());
        verify(workOrderRepository, times(1)).findByMechanic_UserId(1L);
    }

    @Test
    @DisplayName("Should successfully get work order")
    void testGetWorkOrder_Success() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.getWorkOrder("SRV-12345678", 1L);

        // Assert
        assertNotNull(result);
        assertEquals("SRV-12345678", result.getServiceOrderId());
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
    }

    @Test
    @DisplayName("Should throw WorkOrderException when work order not found")
    void testGetWorkOrder_NotFound() {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.getWorkOrder("SRV-12345678", 1L));
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
    }

    @Test
    @DisplayName("Should successfully start work order")
    void testStartWorkOrder_Success() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.startWorkOrder("SRV-12345678", 1L);

        // Assert
        assertNotNull(result);
        assertEquals(WorkOrderStatus.IN_PROGRESS, result.getStatus());
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
        // Note: save() is not called explicitly as JPA handles persistence through @Transactional
    }

    @Test
    @DisplayName("Should throw WorkOrderException when trying to start non-assigned work order")
    void testStartWorkOrder_InvalidStatus() {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> mechanicService.startWorkOrder("SRV-12345678", 1L));
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
    }

//    @Test
//    @DisplayName("Should successfully complete work order")
//    void testCompleteWorkOrder_Success() throws WorkOrderException {
//        // Arrange
//        testWorkOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
//        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
//            .thenReturn(Optional.of(testWorkOrder));
//
//        // Act
//        MechanicWorkOrderResponse result = mechanicService.completeWorkOrder("SRV-12345678", 1L, 1500.0);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(WorkOrderStatus.COMPLETED, result.getStatus());
//        assertEquals(1500.0, result.getFinalCost());
//        verify(workOrderRepository, times(1))
//            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
//        // Note: save() is not called explicitly as JPA handles persistence through @Transactional
//    }

    @Test
    @DisplayName("Should successfully update progress")
    void testUpdateProgress_Success() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.updateProgress("SRV-12345678", 1L, "Progress update");

        // Assert
        assertNotNull(result);
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
        // Note: save() is not called explicitly as JPA handles persistence through @Transactional
    }

    @Test
    @DisplayName("Should successfully complete work order")
    void testCompleteWorkOrder_Success() throws WorkOrderException {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        testWorkOrder.setScheduledAt(OffsetDateTime.now());
        testWorkOrder.setEstimatedCost(1000.0);
        com.automotive.mechanic.entity.Customer testCustomer = new com.automotive.mechanic.entity.Customer();
        testCustomer.setUserEmail("customer@example.com");
        testCustomer.setUserName("Test Customer");
        testVehicle.setOwner(testCustomer);
        
        com.automotive.mechanic.dto.WorkOrderCompletionRequest completionRequest = 
            new com.automotive.mechanic.dto.WorkOrderCompletionRequest();
        completionRequest.setFinalCost(1500.0);
        completionRequest.setServiceDetails("Service completed");
        completionRequest.setServiceImageUrl("http://example.com/image.jpg");
        
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.completeWorkOrder("SRV-12345678", 1L, completionRequest);

        // Assert
        assertNotNull(result);
        assertEquals(WorkOrderStatus.COMPLETED, result.getStatus());
        assertEquals(1500.0, result.getFinalCost());
        assertTrue(testVehicle.getServiceDone());
        assertFalse(testVehicle.getBookedForService());
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
    }

    @Test
    @DisplayName("Should throw WorkOrderException when work order not found for complete")
    void testCompleteWorkOrder_NotFound() {
        // Arrange
        com.automotive.mechanic.dto.WorkOrderCompletionRequest completionRequest = 
            new com.automotive.mechanic.dto.WorkOrderCompletionRequest();
        completionRequest.setFinalCost(1500.0);
        
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class,
            () -> mechanicService.completeWorkOrder("SRV-12345678", 1L, completionRequest));
        verify(workOrderRepository, times(1))
            .findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L);
    }

    @Test
    @DisplayName("Should throw WorkOrderException when work order not in progress")
    void testCompleteWorkOrder_InvalidStatus() {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
        com.automotive.mechanic.dto.WorkOrderCompletionRequest completionRequest = 
            new com.automotive.mechanic.dto.WorkOrderCompletionRequest();
        completionRequest.setFinalCost(1500.0);
        
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act & Assert
        assertThrows(WorkOrderException.class,
            () -> mechanicService.completeWorkOrder("SRV-12345678", 1L, completionRequest));
    }

    @Test
    @DisplayName("Should handle null service details in completion request")
    void testCompleteWorkOrder_NullServiceDetails() throws WorkOrderException {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        testWorkOrder.setScheduledAt(OffsetDateTime.now());
        com.automotive.mechanic.entity.Customer testCustomer = new com.automotive.mechanic.entity.Customer();
        testCustomer.setUserEmail("customer@example.com");
        testCustomer.setUserName("Test Customer");
        testVehicle.setOwner(testCustomer);
        
        com.automotive.mechanic.dto.WorkOrderCompletionRequest completionRequest = 
            new com.automotive.mechanic.dto.WorkOrderCompletionRequest();
        completionRequest.setFinalCost(1500.0);
        completionRequest.setServiceDetails(null);
        
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.completeWorkOrder("SRV-12345678", 1L, completionRequest);

        // Assert
        assertNotNull(result);
        assertEquals(WorkOrderStatus.COMPLETED, result.getStatus());
    }

    @Test
    @DisplayName("Should handle null description when appending service details")
    void testCompleteWorkOrder_NullDescription() throws WorkOrderException {
        // Arrange
        testWorkOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        testWorkOrder.setDescription(null);
        testWorkOrder.setScheduledAt(OffsetDateTime.now());
        com.automotive.mechanic.entity.Customer testCustomer = new com.automotive.mechanic.entity.Customer();
        testCustomer.setUserEmail("customer@example.com");
        testCustomer.setUserName("Test Customer");
        testVehicle.setOwner(testCustomer);
        
        com.automotive.mechanic.dto.WorkOrderCompletionRequest completionRequest = 
            new com.automotive.mechanic.dto.WorkOrderCompletionRequest();
        completionRequest.setFinalCost(1500.0);
        completionRequest.setServiceDetails("Service completed");
        
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.completeWorkOrder("SRV-12345678", 1L, completionRequest);

        // Assert
        assertNotNull(result);
        assertTrue(testWorkOrder.getDescription().contains("Service Details: Service completed"));
    }

    @Test
    @DisplayName("Should return empty list when no work orders assigned")
    void testListAssignedWorkOrders_EmptyList() {
        // Arrange
        when(workOrderRepository.findByMechanic_UserId(1L)).thenReturn(new ArrayList<>());

        // Act
        List<MechanicWorkOrderResponse> result = mechanicService.listAssignedWorkOrders(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(workOrderRepository, times(1)).findByMechanic_UserId(1L);
    }

    @Test
    @DisplayName("Should handle null description when updating progress")
    void testUpdateProgress_NullDescription() throws WorkOrderException {
        // Arrange
        testWorkOrder.setDescription(null);
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.updateProgress("SRV-12345678", 1L, "Progress update");

        // Assert
        assertNotNull(result);
        assertTrue(testWorkOrder.getDescription().contains("Progress update"));
    }

    @Test
    @DisplayName("Should handle null progress update")
    void testUpdateProgress_NullProgress() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderIdAndMechanic_UserId("SRV-12345678", 1L))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        MechanicWorkOrderResponse result = mechanicService.updateProgress("SRV-12345678", 1L, null);

        // Assert
        assertNotNull(result);
    }
}

