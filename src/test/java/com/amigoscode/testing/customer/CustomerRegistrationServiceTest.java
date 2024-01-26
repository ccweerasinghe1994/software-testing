package com.amigoscode.testing.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerRegistrationService underTest;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {

        // given a phone number and a customer
        String phoneNumber = "123456789";
        Customer customer = new Customer(
                UUID.randomUUID(),
                "Jamila",
                phoneNumber
        );

//        ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // when
        underTest.registerNewCustomer(request);

        // then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertEquals(customerArgumentCaptorValue, customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerAlreadyExists() {
        // given a phone number and a customer
        String phoneNumber = "123456789";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(
                id,
                "Jamila",
                phoneNumber
        );

//        ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        // when
        underTest.registerNewCustomer(request);

        // then
//        then(customerRepository).should(never()).save(any());
        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
        then(customerRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldThrowAnExceptionWhenThePhoneNumberIsAlreadyTaken() {
        // given a phone number and a customer
        String phoneNumber = "123456789";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(
                id,
                "Jamila",
                phoneNumber
        );
        Customer result = new Customer(
                id,
                "Jamila1",
                phoneNumber
        );
//        ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(result));

//        assertThrows(IllegalStateException.class, () -> underTest.registerNewCustomer(request));
        Assertions.assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("phone number taken");

//        finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}