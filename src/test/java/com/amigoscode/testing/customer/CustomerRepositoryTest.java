package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // given
        // when
        // then
    }

    @Test
    void itShouldSaveCustomer() {

        // given
        UUID id = UUID.randomUUID();
        Customer jamila = new Customer(id, "Jamila", "123456789");

        // when
        underTest.save(jamila);

        // then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer).isPresent().hasValueSatisfying(
                customer -> {

//                    manually check each field

                    assertThat(customer.getId()).isEqualTo(id);
                    assertThat(customer.getName()).isEqualTo("Jamila");
                    assertThat(customer.getPhoneNumber()).isEqualTo("123456789");

//                    use isEqualToComparingFieldByField to check each field

                    
                    assertThat(customer).isEqualToComparingFieldByField(jamila);
                }
        );

    }
}