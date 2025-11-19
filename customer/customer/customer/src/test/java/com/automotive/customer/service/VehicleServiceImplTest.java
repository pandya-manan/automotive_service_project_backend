package com.automotive.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.automotive.customer.entity.Customer;
import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.repository.CustomerRepository;
import com.automotive.customer.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehicleServiceImpl Tests")
class VehicleServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Customer testCustomer;
    private Vehicle testVehicle;

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
    }

    @Test
    @DisplayName("Should successfully add vehicle to customer")
    void testAddVehicleToCustomer_Success() throws CustomerDoesNotExistException {
        // Arrange
        when(customerRepository.findCustomerByUserId(1L)).thenReturn(testCustomer);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        // Act
        Vehicle result = vehicleService.addVehicleToCustomer(1L, testVehicle);

        // Assert
        assertNotNull(result);
        assertEquals(testVehicle.getVehicleId(), result.getVehicleId());
        verify(customerRepository, times(1)).findCustomerByUserId(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should throw CustomerDoesNotExistException when customer not found")
    void testAddVehicleToCustomer_CustomerNotFound() {
        // Arrange
        when(customerRepository.findCustomerByUserId(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomerDoesNotExistException.class, 
            () -> vehicleService.addVehicleToCustomer(1L, testVehicle));
        verify(customerRepository, times(1)).findCustomerByUserId(1L);
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully get all vehicles for customer")
    void testGetAllVehiclesForCustomer_Success() {
        // Arrange
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(testVehicle);
        when(vehicleRepository.findByOwner_UserId(1L)).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.getAllVehiclesForCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testVehicle.getVehicleId(), result.get(0).getVehicleId());
        verify(vehicleRepository, times(1)).findByOwner_UserId(1L);
    }

    @Test
    @DisplayName("Should successfully update vehicle")
    void testUpdateVehicle_Success() throws CustomerDoesNotExistException, VehicleDoesNotExistException {
        // Arrange
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setVin("NEWVIN123");
        updatedVehicle.setMake("Honda");
        updatedVehicle.setModel("Accord");
        updatedVehicle.setRegistrationNumber("XYZ789");

        when(vehicleRepository.findVehicleByVehicleId(1L)).thenReturn(testVehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        // Act
        Vehicle result = vehicleService.updateVehicle(1L, updatedVehicle);

        // Assert
        assertNotNull(result);
        verify(vehicleRepository, times(1)).findVehicleByVehicleId(1L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should throw VehicleDoesNotExistException when vehicle not found for update")
    void testUpdateVehicle_VehicleNotFound() {
        // Arrange
        when(vehicleRepository.findVehicleByVehicleId(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(VehicleDoesNotExistException.class, 
            () -> vehicleService.updateVehicle(1L, testVehicle));
        verify(vehicleRepository, times(1)).findVehicleByVehicleId(1L);
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should successfully delete vehicle")
    void testDeleteVehicle_Success() throws VehicleDoesNotExistException {
        // Arrange
        when(vehicleRepository.findVehicleByVehicleId(1L)).thenReturn(testVehicle);
        doNothing().when(vehicleRepository).delete(any(Vehicle.class));

        // Act
        vehicleService.deleteVehicle(1L);

        // Assert
        verify(vehicleRepository, times(1)).findVehicleByVehicleId(1L);
        verify(vehicleRepository, times(1)).delete(testVehicle);
    }

    @Test
    @DisplayName("Should throw VehicleDoesNotExistException when vehicle not found for delete")
    void testDeleteVehicle_VehicleNotFound() {
        // Arrange
        when(vehicleRepository.findVehicleByVehicleId(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(VehicleDoesNotExistException.class, 
            () -> vehicleService.deleteVehicle(1L));
        verify(vehicleRepository, times(1)).findVehicleByVehicleId(1L);
        verify(vehicleRepository, never()).delete(any());
    }
}


