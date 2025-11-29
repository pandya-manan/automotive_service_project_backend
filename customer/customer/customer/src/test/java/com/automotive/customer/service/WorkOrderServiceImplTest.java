package com.automotive.customer.service;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import com.automotive.customer.entity.Customer;
import com.automotive.customer.entity.ServiceStatusProjection;
import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.entity.WorkOrder;
import com.automotive.customer.entity.WorkOrderRequest;
import com.automotive.customer.entity.WorkOrderResponseDto;
import com.automotive.customer.entity.WorkOrderStatus;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.repository.CustomerRepository;
import com.automotive.customer.repository.VehicleRepository;
import com.automotive.customer.repository.WorkOrderRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkOrderServiceImpl Tests")
class WorkOrderServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private WorkOrderRepository workOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private WorkOrderServiceImpl workOrderService;

    private Customer testCustomer;
    private Vehicle testVehicle;
    private WorkOrderRequest workOrderRequest;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setUserId(1L);
        testCustomer.setUserName("Test Customer");
        testCustomer.setUserEmail("test@example.com");

        testVehicle = new Vehicle();
        testVehicle.setVehicleId(1L);
        testVehicle.setVin("VIN123456");
        testVehicle.setMake("Toyota");
        testVehicle.setModel("Camry");
        testVehicle.setRegistrationNumber("ABC123");
        testVehicle.setOwner(testCustomer);
        testVehicle.setBookedForService(false);
        testVehicle.setServiceDone(true); // Service is done, so vehicle can be booked again

        workOrderRequest = new WorkOrderRequest();
        workOrderRequest.setDescription("Test service");
        workOrderRequest.setScheduledAt(OffsetDateTime.now().plusDays(1));
    }

    @Test
    @DisplayName("Should successfully create work order")
    void testCreateWorkOrderForCustomer_Success() throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        // Arrange
        when(vehicleRepository.findByVehicleIdAndOwnerUserId(1L, 1L))
            .thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        when(workOrderRepository.save(any(WorkOrder.class))).thenAnswer(invocation -> {
            WorkOrder wo = invocation.getArgument(0);
            wo.setId(1L);
            wo.setServiceOrderId("SRV-12345678");
            return wo;
        });
        // Mock RestClient chain - use lenient to avoid unnecessary stubbing issues
        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(null);

        // Act
        WorkOrderResponseDto result = workOrderService.createWorkOrderForCustomer(1L, 1L, workOrderRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getWorkOrderId());
        verify(vehicleRepository, times(1)).findByVehicleIdAndOwnerUserId(1L, 1L);
        verify(workOrderRepository, times(1)).save(any(WorkOrder.class));
    }

    @Test
    @DisplayName("Should throw VehicleDoesNotExistException when vehicle not found")
    void testCreateWorkOrderForCustomer_VehicleNotFound() {
        // Arrange
        when(vehicleRepository.findByVehicleIdAndOwnerUserId(1L, 1L))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(VehicleDoesNotExistException.class, 
            () -> workOrderService.createWorkOrderForCustomer(1L, 1L, workOrderRequest));
        verify(vehicleRepository, times(1)).findByVehicleIdAndOwnerUserId(1L, 1L);
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw VehicleDoesNotExistException when vehicle already booked")
    void testCreateWorkOrderForCustomer_VehicleAlreadyBooked() {
        // Arrange
        testVehicle.setBookedForService(true);
        when(vehicleRepository.findByVehicleIdAndOwnerUserId(1L, 1L))
            .thenReturn(Optional.of(testVehicle));

        // Act & Assert
        assertThrows(VehicleDoesNotExistException.class, 
            () -> workOrderService.createWorkOrderForCustomer(1L, 1L, workOrderRequest));
        verify(vehicleRepository, times(1)).findByVehicleIdAndOwnerUserId(1L, 1L);
        verify(workOrderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully get service status for customer")
    void testGetServiceStatusForCustomer_Success() throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        // Arrange - need at least one projection since empty list throws exception
        List<ServiceStatusProjection> projections = new ArrayList<>();
        ServiceStatusProjection projection = mock(ServiceStatusProjection.class);
        projections.add(projection);
        when(workOrderRepository.findServiceStatusNative(1L)).thenReturn(projections);
        when(customerRepository.findCustomerByUserId(1L)).thenReturn(testCustomer);

        // Act
        List<ServiceStatusProjection> result = workOrderService.getServiceStatusForCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(workOrderRepository, times(1)).findServiceStatusNative(1L);
        verify(customerRepository, times(1)).findCustomerByUserId(1L);
    }
    
    @Test
    @DisplayName("Should throw VehicleDoesNotExistException when no service data found")
    void testGetServiceStatusForCustomer_NoServiceData() {
        // Arrange
        List<ServiceStatusProjection> projections = new ArrayList<>(); // Empty list
        when(workOrderRepository.findServiceStatusNative(1L)).thenReturn(projections);
        when(customerRepository.findCustomerByUserId(1L)).thenReturn(testCustomer);

        // Act & Assert
        assertThrows(VehicleDoesNotExistException.class, 
            () -> workOrderService.getServiceStatusForCustomer(1L));
        verify(workOrderRepository, times(1)).findServiceStatusNative(1L);
        verify(customerRepository, times(1)).findCustomerByUserId(1L);
    }

    @Test
    @DisplayName("Should throw CustomerDoesNotExistException when customer not found")
    void testGetServiceStatusForCustomer_CustomerNotFound() {
        // Arrange - need non-empty list to reach customer check
        List<ServiceStatusProjection> projections = new ArrayList<>();
        ServiceStatusProjection projection = mock(ServiceStatusProjection.class);
        projections.add(projection);
        when(workOrderRepository.findServiceStatusNative(1L)).thenReturn(projections);
        when(customerRepository.findCustomerByUserId(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerDoesNotExistException.class, 
            () -> workOrderService.getServiceStatusForCustomer(1L));
        verify(customerRepository, times(1)).findCustomerByUserId(1L);
    }
}

