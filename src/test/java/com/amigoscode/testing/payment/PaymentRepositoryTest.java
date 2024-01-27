package com.amigoscode.testing.payment;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldInsertPayment() {

        // given
        long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                UUID.fromString("123e4567-e89b-42d3-a456-556642440000"),
                new BigDecimal("10.00"),
                Currency.USD,
                "card",
                "Donation"
        );
        // when
        underTest.save(payment);
        // then
        Optional<Payment> optionalPayment = underTest.findById(paymentId);
        assertThat(optionalPayment)
                .isPresent()
                .hasValueSatisfying(
                        p -> assertThat(p).isEqualToComparingFieldByField(payment)
                );
    }
}