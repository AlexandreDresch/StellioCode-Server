package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan createPlan(Plan plan) {
        if (planRepository.existsByName(plan.getName())) {
            throw new IllegalArgumentException("Plan with this name already exists");
        }
        return planRepository.save(plan);
    }

    public Plan updatePlan(UUID id, Plan updatedPlan) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));

        existingPlan.setName(updatedPlan.getName());
        existingPlan.setDescription(updatedPlan.getDescription());
        existingPlan.setPrice(updatedPlan.getPrice());
        existingPlan.setFeatures(updatedPlan.getFeatures());
        return planRepository.save(existingPlan);
    }

    public void deletePlan(UUID id) {
        if (!planRepository.existsById(id)) {
            throw new EntityNotFoundException("Plan not found with id: " + id);
        }
        planRepository.deleteById(id);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public List<Map<String, Object>> getPlanStatistics() {
        return planRepository.findPlanStatistics();
    }

    public Plan getPlanWithFaqs(UUID id) {
        return planRepository.findByIdWithFaqs(id);
    }
}
