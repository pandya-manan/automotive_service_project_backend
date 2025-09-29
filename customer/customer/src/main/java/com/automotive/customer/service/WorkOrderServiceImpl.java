package com.automotive.customer.service;

import com.automotive.customer.entity.*;
import com.automotive.customer.exception.CustomerDoesNotExistException;
import com.automotive.customer.exception.VehicleDoesNotExistException;
import com.automotive.customer.repository.CustomerRepository;
import com.automotive.customer.repository.VehicleRepository;
import com.automotive.customer.repository.WorkOrderRepository;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {
	
	private static final Logger logger = Logger.getLogger(WorkOrderServiceImpl.class.getName());

    @Autowired
    private VehicleRepository vehicleRepo;

    @Autowired
    private WorkOrderRepository workOrderRepo;
    
    @Autowired 
    private CustomerRepository customerRepo;
    
    @Autowired
    private RestClient restClient;

    @Value("${email-url}")
    private String EMAIL_SERVICE_URL;

    @Override
    @Transactional
    public WorkOrderResponseDto createWorkOrderForCustomer(Long customerId, Long vehicleId, WorkOrderRequest request) throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        Vehicle vehicle=vehicleRepo.findByVehicleIdAndOwnerUserId(vehicleId,customerId).orElseThrow(()->new VehicleDoesNotExistException("There is no vehicle with the id: "+vehicleId));
        if(vehicle.getBookedForService()== Boolean.TRUE || vehicle.getServiceDone()== Boolean.FALSE)
        {
            throw new VehicleDoesNotExistException("Vehicle is already booked for service. Let the service be completed");
        }
        vehicle.setBookedForService(Boolean.TRUE);
        vehicle.setServiceDone(Boolean.FALSE);
        vehicle.setUpdatedAt(OffsetDateTime.now());

        WorkOrder workOrder=new WorkOrder();
        workOrder.setVehicle(vehicle);
        workOrder.setDescription(request.getDescription());
        workOrder.setScheduledAt(request.getScheduledAt());
        workOrder.setStatus(WorkOrderStatus.OPEN);
        logger.info("Work Order Generated for Vehicle Registration Number: "+vehicle.getRegistrationNumber());
        String generatedServiceBookingId=generateServiceBookingId();
        logger.info("Service Booking Id generated as: "+generatedServiceBookingId);
        workOrder.setServiceOrderId(generatedServiceBookingId);
        vehicleRepo.save(vehicle);
        WorkOrder savedBooking=workOrderRepo.save(workOrder);
        ServiceBookingRequestDTO emailServiceBookingRequest=new ServiceBookingRequestDTO();
        emailServiceBookingRequest.setCreatedAt(savedBooking.getCreatedAt());
        emailServiceBookingRequest.setMake(vehicle.getMake());
        emailServiceBookingRequest.setModel(vehicle.getModel());
        emailServiceBookingRequest.setVehicleVin(vehicle.getVin());
        emailServiceBookingRequest.setTo(vehicle.getOwner().getUserEmail());
        emailServiceBookingRequest.setServiceOrderId(generatedServiceBookingId);
        emailServiceBookingRequest.setUserName(vehicle.getOwner().getUserName());
        logger.info("Email Service Request generated: "+emailServiceBookingRequest.toString());
        try
        {
        	restClient.post().uri(EMAIL_SERVICE_URL).body(emailServiceBookingRequest).retrieve().toEntity(String.class);
        	logger.info("Email with service details sent to: "+emailServiceBookingRequest.getTo());
        }
        catch(Exception e)
        {
        	logger.warning("Failed to send email due to: "+e.getMessage());
        }
        return new WorkOrderResponseDto(savedBooking.getId(), vehicle.getVehicleId(),savedBooking.getStatus(),savedBooking.getScheduledAt(),savedBooking.getDescription());

    }
    
    private static String generateServiceBookingId()
    {
    	return "SRV-"+UUID.randomUUID().toString().substring(0, 8);
    }

    public List<ServiceStatusProjection> getServiceStatusForCustomer(Long userId) throws VehicleDoesNotExistException, CustomerDoesNotExistException {
        List<ServiceStatusProjection> result= workOrderRepo.findServiceStatusNative(userId);
        Customer customer=customerRepo.findCustomerByUserId(userId);
        if(result.isEmpty() || result==null)
        {
        	throw new VehicleDoesNotExistException("There is no service data found for customer "+customer.getUserEmail()+" "+customer.getUserName());       
        	
        }
        if(customer==null)
        {
        	throw new CustomerDoesNotExistException("There is no customer with this id in our system");
        }
        return result;
    }
}
