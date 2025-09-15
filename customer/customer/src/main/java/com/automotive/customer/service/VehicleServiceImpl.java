package com.automotive.customer.service;

import com.automotive.customer.entity.Customer;
import com.automotive.customer.entity.Vehicle;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.repository.CustomerRepository;
import com.automotive.customer.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService{

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private VehicleRepository vehicleRepo;

    @Override
    public Vehicle addVehicleToCustomer(Long customerId, Vehicle vehicle) throws CustomerDoesNotExistException {
        Customer customer=customerRepo.findCustomerByUserId(customerId);
        if(customer==null)
        {
            throw new CustomerDoesNotExistException("There is no customer in the database for the given customerId: "+customerId);
        }
        vehicle.setOwner(customer);
        customer.addVehicle(vehicle);
        return vehicleRepo.save(vehicle);

    }

    @Override
    public List<Vehicle> getAllVehiclesForCustomer(Long customerId) {
        return vehicleRepo.findByOwner_UserId(customerId);
    }

    @Override
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) throws CustomerDoesNotExistException, VehicleDoesNotExistException {
        Vehicle existing=vehicleRepo.findVehicleByVehicleId(vehicleId);
        if(existing==null)
        {
            throw new VehicleDoesNotExistException("There is no vehicle with this vehicle Id: "+vehicleId);
        }
        existing.setVin(updatedVehicle.getVin());
        existing.setMake(updatedVehicle.getMake());
        existing.setModel(updatedVehicle.getModel());
        existing.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
        existing.setInsured(updatedVehicle.getInsured());
        existing.setYearOfRegistration(updatedVehicle.getYearOfRegistration());
        return vehicleRepo.save(existing);
    }

    @Override
    public void deleteVehicle(Long vehicleId) throws VehicleDoesNotExistException {
        Vehicle existing=vehicleRepo.findVehicleByVehicleId(vehicleId);
        if(existing==null)
        {
            throw new VehicleDoesNotExistException("There is no vehicle with this vehicle Id: "+vehicleId);
        }
        vehicleRepo.delete(existing);
    }
}
