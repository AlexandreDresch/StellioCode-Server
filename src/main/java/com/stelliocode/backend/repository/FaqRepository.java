package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {
    @Query("SELECT COUNT(f) > 0 FROM Faq f WHERE f.entityType = 'Plan' AND f.entityId = :entityId AND f.question = :question")
    boolean existsByPlanAndQuestion(@Param("entityId") UUID entityId, @Param("question") String question);

    @Query("SELECT COUNT(f) > 0 FROM Faq f WHERE f.entityType = 'Service' AND f.entityId = :entityId AND f.question = :question")
    boolean existsByServiceAndQuestion(@Param("entityId") UUID entityId, @Param("question") String question);

    @Query("SELECT f FROM Faq f WHERE f.entityType = 'Plan' AND f.entityId = :entityId")
    List<Faq> findByPlanId(@Param("entityId") UUID planId);

    @Query("SELECT f FROM Faq f WHERE f.entityType = 'Service' AND f.entityId = :entityId")
    List<Faq> findByServiceId(@Param("entityId") UUID serviceId);
}