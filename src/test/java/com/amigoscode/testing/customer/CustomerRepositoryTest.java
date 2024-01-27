package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // given
        UUID id = UUID.randomUUID();
        String phoneNumber = "123456789";
        Customer jamila = new Customer(id, "Jamila", phoneNumber);

        // when
        underTest.save(jamila);

        // then
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
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

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenPhoneNumberDoesntExists() {
        // given
        String phoneNumber = "123456789";

        // when
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);

        // then
        assertThat(optionalCustomer).isNotPresent();
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

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "0000");

        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);


    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Andera", null);

        // Then
        assertThatThrownBy(() -> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);

    }

}