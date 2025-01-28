package com.stelliocode.backend.repository;

import com.stelliocode.backend.entity.Faq;
import com.stelliocode.backend.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FaqRepository extends JpaRepository<Faq, UUID> {
    boolean existsByPlanAndQuestion(Plan plan, String question);
}