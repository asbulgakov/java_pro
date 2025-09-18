package ru.bulgakov.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bulgakov.spring.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
