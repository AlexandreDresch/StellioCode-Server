package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            "COALESCE(SUM(CASE WHEN pr.status IN ('IN_PROGRESS', 'COMPLETED') THEN pr.price ELSE 0 END), 0) AS monthlyRevenue " +
            "FROM Plan p " +
            "LEFT JOIN Project pr ON p.id = pr.plan.id " +
            "AND EXTRACT(MONTH FROM pr.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND pr.status IN ('IN_PROGRESS', 'COMPLETED') " +
            "GROUP BY p.name " +
            "ORDER BY totalContracts DESC")
    List<Map<String, Object>> findPlanStatistics();

}
