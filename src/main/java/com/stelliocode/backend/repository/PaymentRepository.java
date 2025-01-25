package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'paid'")
    Double calculateTotalRevenue();
}
