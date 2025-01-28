package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Plan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface PlanRepository extends JpaRepository<Plan, UUID> {
    boolean existsByName(String name);

    @Query("SELECT " +
            "p.name AS planName, " +
            "COUNT(pr.id) AS totalContracts, " +
            "SUM(pr.price) AS totalRevenue " +
            "FROM Project pr " +
            "JOIN pr.plan p " +
            "GROUP BY p.name " +
            "ORDER BY totalContracts DESC")
    List<Map<String, Object>> findPlanStatistics();

    @Query("SELECT p FROM Plan p LEFT JOIN FETCH p.faqs WHERE p.id = :id")
    Plan findByIdWithFaqs(@Param("id") UUID id);
}
