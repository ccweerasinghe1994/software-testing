package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerRegistrationService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) throws IllegalStateException {
        String requestPhoneNumber = request.getCustomer().getPhoneNumber();
        customerRepository.selectCustomerByPhoneNumber(requestPhoneNumber)
                .ifPresentOrElse(customer -> {
                    String requestCustomerName = request.getCustomer().getName();
                    if (customer.getName().equals(requestCustomerName)) {
                        System.out.println("Customer already exists");
                        return;
                    }
                    throw new IllegalStateException("phone number taken");

                }, () -> {

                    if (request.getCustomer().getId() == null) {
                        request.getCustomer().setId(UUID.randomUUID());
                    }

                    customerRepository.save(request.getCustomer());
                })
        ;
    }
}
