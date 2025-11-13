package org.example.repo;

import org.example.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentJpaRepo extends JpaRepository<Payment, Long> {

    List<Payment> findByPaymentMethodIgnoreCase(String paymentMethod);
}

