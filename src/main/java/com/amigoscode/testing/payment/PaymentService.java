package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private CustomerRepository customerRepository;

    private PaymentRepository paymentRepository;

    private CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(CustomerRepository customerRepository, PaymentRepository paymentRepository, CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
//        does customer exist?
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        optionalCustomer.ifPresent(customer -> {
            Payment payment = paymentRequest.getPayment();
            cardPaymentCharger.chargeCard(
                    payment.getSource(),
                    payment.getAmount(),
                    payment.getCurrency(),
                    payment.getDescription()
            );
            paymentRepository.save(payment);
        });
        optionalCustomer.orElseThrow(() -> new IllegalStateException(String.format("Customer with id %s not found", customerId)));
//        do we support the currency? if not throw
//        charge card
//        if not debited throw
//        insert payment
//        TODO send sms to customer

    }
}
