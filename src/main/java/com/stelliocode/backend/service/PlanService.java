package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.CreatePlanRequestDTO;
import com.stelliocode.backend.dto.FaqResponseDTO;
import com.stelliocode.backend.dto.PlanResponseDTO;
import com.stelliocode.backend.dto.PlanWithFaqsResponseDTO;
import com.stelliocode.backend.entity.Faq;
import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.repository.FaqRepository;
import com.stelliocode.backend.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final FaqRepository faqRepository;

    @Transactional
    public PlanResponseDTO createPlan(CreatePlanRequestDTO request) {
        if (planRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Plan with this name already exists");
        }

        Plan plan = Plan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .yearlyPrice(request.getYearlyPrice())
                .period(request.getPeriod())
                .features(request.getFeatures())
                .isPopular(request.isPopular())
                .build();

        Plan savedPlan = planRepository.save(plan);
        return mapToPlanResponseDTO(savedPlan);
    }

    @Transactional
    public PlanResponseDTO updatePlan(UUID id, CreatePlanRequestDTO request) {
        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));

        existingPlan.setName(request.getName());
        existingPlan.setDescription(request.getDescription());
        existingPlan.setPrice(request.getPrice());
        existingPlan.setYearlyPrice(request.getYearlyPrice());
        existingPlan.setPeriod(request.getPeriod());
        existingPlan.setFeatures(request.getFeatures());
        existingPlan.setPopular(request.isPopular());

        Plan updatedPlan = planRepository.save(existingPlan);
        return mapToPlanResponseDTO(updatedPlan);
    }

    @Transactional
    public void deletePlan(UUID id) {
        if (!planRepository.existsById(id)) {
            throw new EntityNotFoundException("Plan not found with id: " + id);
        }
        planRepository.deleteById(id);
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::mapToPlanResponseDTO)
                .toList();
    }

    public List<Map<String, Object>> getPlanStatistics() {
        return planRepository.findPlanStatistics();
    }

    public PlanWithFaqsResponseDTO getPlanWithFaqs(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));

        List<Faq> faqs = faqRepository.findByPlanId(id);

        return mapToPlanWithFaqsResponseDTO(plan, faqs);
    }

    public PlanResponseDTO getPlanById(UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found with id: " + id));
        return mapToPlanResponseDTO(plan);
    }

    private PlanResponseDTO mapToPlanResponseDTO(Plan plan) {
        return PlanResponseDTO.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .yearlyPrice(plan.getYearlyPrice())
                .period(plan.getPeriod())
                .features(plan.getFeatures())
                .isPopular(plan.isPopular())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    private PlanWithFaqsResponseDTO mapToPlanWithFaqsResponseDTO(Plan plan, List<Faq> faqs) {
        return PlanWithFaqsResponseDTO.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .price(plan.getPrice())
                .yearlyPrice(plan.getYearlyPrice())
                .period(plan.getPeriod())
                .features(plan.getFeatures())
                .isPopular(plan.isPopular())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .faqs(faqs.stream()
                        .map(this::mapToFaqResponseDTO)
                        .toList())
                .build();
    }

    private FaqResponseDTO mapToFaqResponseDTO(Faq faq) {
        return FaqResponseDTO.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .build();
    }
}