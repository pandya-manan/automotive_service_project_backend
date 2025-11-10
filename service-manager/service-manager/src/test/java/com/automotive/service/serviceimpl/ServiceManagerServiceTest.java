package com.automotive.service.serviceimpl;

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
import org.springframework.web.client.RestClient;

import com.automotive.service.dto.AssignManagerRequest;
import com.automotive.service.dto.AssignMechanicRequest;
import com.automotive.service.dto.SimpleIdRequest;
import com.automotive.service.dto.UpdateCostsRequest;
import com.automotive.service.dto.WorkOrderResponseDto;
import com.automotive.service.entity.Mechanic;
import com.automotive.service.entity.ServiceManager;
import com.automotive.service.entity.Vehicle;
import com.automotive.service.entity.WorkOrder;
import com.automotive.service.entity.WorkOrderStatus;
import com.automotive.service.exception.WorkOrderException;
import com.automotive.service.repository.MechanicRepository;
import com.automotive.service.repository.ServiceManagerRepository;
import com.automotive.service.repository.VehicleRepository;
import com.automotive.service.repository.WorkOrderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceManagerService Tests")
class ServiceManagerServiceTest {

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
    private ServiceManager testManager;
    private Mechanic testMechanic;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        serviceManagerService = new ServiceManagerService(
            workOrderRepository,
            vehicleRepository,
            serviceManagerRepository,
            mechanicRepository,
            restClient
        );

        testManager = new ServiceManager();
        testManager.setUserId(1L);
        testManager.setUserName("Test Manager");

        testMechanic = new Mechanic();
        testMechanic.setUserId(2L);
        testMechanic.setUserName("Test Mechanic");

        testVehicle = new Vehicle();
        testVehicle.setVehicleId(1L);
        testVehicle.setVin("VIN123");

        testWorkOrder = new WorkOrder();
        testWorkOrder.setId(1L);
        testWorkOrder.setServiceOrderId("SRV-12345678");
        testWorkOrder.setStatus(WorkOrderStatus.OPEN);
        testWorkOrder.setVehicle(testVehicle);
    }

    @Test
    @DisplayName("Should successfully list open work orders")
    void testListOpenWorkOrders_Success() {
        // Arrange
        List<WorkOrder> workOrders = new ArrayList<>();
        workOrders.add(testWorkOrder);
        when(workOrderRepository.findByStatus(WorkOrderStatus.OPEN)).thenReturn(workOrders);

        // Act
        List<WorkOrderResponseDto> result = serviceManagerService.listOpenWorkOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(workOrderRepository, times(1)).findByStatus(WorkOrderStatus.OPEN);
    }

    @Test
    @DisplayName("Should successfully get work order by service order ID")
    void testGetByServiceOrderId_Success() throws WorkOrderException {
        // Arrange
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(Optional.of(testWorkOrder));

        // Act
        WorkOrderResponseDto result = serviceManagerService.getByServiceOrderId("SRV-12345678");

        // Assert
        assertNotNull(result);
        assertEquals("SRV-12345678", result.serviceOrderId);
        verify(workOrderRepository, times(1)).findByServiceOrderId("SRV-12345678");
    }

    @Test
    @DisplayName("Should throw WorkOrderException when work order not found")
    void testGetByServiceOrderId_NotFound() {
        // Arrange
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WorkOrderException.class, 
            () -> serviceManagerService.getByServiceOrderId("SRV-12345678"));
        verify(workOrderRepository, times(1)).findByServiceOrderId("SRV-12345678");
    }

    @Test
    @DisplayName("Should successfully assign manager to work order")
    void testAssignManager_Success() throws WorkOrderException {
        // Arrange
        AssignManagerRequest request = new AssignManagerRequest(1L);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(Optional.of(testWorkOrder));
        when(serviceManagerRepository.findById(1L))
            .thenReturn(Optional.of(testManager));
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(testWorkOrder);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        // Act
        WorkOrderResponseDto result = serviceManagerService.assignManager("SRV-12345678", request);

        // Assert
        assertNotNull(result);
        verify(workOrderRepository, times(1)).findByServiceOrderId("SRV-12345678");
        verify(serviceManagerRepository, times(1)).findById(1L);
        verify(workOrderRepository, times(1)).save(any(WorkOrder.class));
    }

    @Test
    @DisplayName("Should successfully start work order")
    void testStartWorkOrder_Success() throws WorkOrderException {
        // Arrange
        SimpleIdRequest request = new SimpleIdRequest(1L);
        testWorkOrder.setStatus(WorkOrderStatus.ASSIGNED);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(Optional.of(testWorkOrder));
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(testWorkOrder);

        // Act
        WorkOrderResponseDto result = serviceManagerService.startWorkOrder("SRV-12345678", request);

        // Assert
        assertNotNull(result);
        verify(workOrderRepository, times(1)).findByServiceOrderId("SRV-12345678");
        verify(workOrderRepository, times(1)).save(any(WorkOrder.class));
    }

    @Test
    @DisplayName("Should successfully update costs")
    void testUpdateCosts_Success() throws WorkOrderException {
        // Arrange
        UpdateCostsRequest request = new UpdateCostsRequest(1000.0, 1200.0);
        when(workOrderRepository.findByServiceOrderId("SRV-12345678"))
            .thenReturn(Optional.of(testWorkOrder));
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(testWorkOrder);

        // Act
        WorkOrderResponseDto result = serviceManagerService.updateCosts("SRV-12345678", request);

        // Assert
        assertNotNull(result);
        verify(workOrderRepository, times(1)).findByServiceOrderId("SRV-12345678");
        verify(workOrderRepository, times(1)).save(any(WorkOrder.class));
    }
}


