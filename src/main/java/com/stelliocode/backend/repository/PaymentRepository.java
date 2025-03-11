package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentStatus = 'paid'")
    Double calculateTotalRevenue();

    @Query("SELECT p FROM Payment p " +
            "JOIN p.project pr " +
            "JOIN p.client c " +
            "WHERE pr.id = :projectId AND c.googleId = :googleId")
    Optional<Payment> findByProjectIdAndClientGoogleId(
            @Param("projectId") UUID projectId,
            @Param("googleId") String googleId
    );

    @Query("SELECT p FROM Payment p " +
            "JOIN p.project pr " +
            "JOIN DeveloperProject dp ON dp.project.id = pr.id " +
            "WHERE pr.id = :projectId AND dp.developer.id = :developerId")
    Optional<Payment> findByProjectIdAndDeveloperId(
            @Param("projectId") UUID projectId,
            @Param("developerId") UUID developerId
    );

    Optional<Payment> findByProjectId(UUID projectId);
}
