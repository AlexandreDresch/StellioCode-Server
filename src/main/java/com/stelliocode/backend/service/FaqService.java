package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Faq;
import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.repository.FaqRepository;
import com.stelliocode.backend.repository.PlanRepository;
import com.stelliocode.backend.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final PlanRepository planRepository;
    private final ServiceRepository serviceRepository;

    public Faq addFaqToPlan(UUID planId, String question, String answer) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found."));

        if (faqRepository.existsByPlanAndQuestion(planId, question)) {
            throw new IllegalArgumentException("This plan already has this question.");
        }

        Faq faq = new Faq();
        faq.setQuestion(question);
        faq.setAnswer(answer);
        faq.associateWithPlan(plan);

        return faqRepository.save(faq);
    }

    public Faq addFaqToService(UUID serviceId, String question, String answer) {

         com.stelliocode.backend.entity.Service service = serviceRepository.findById(serviceId)
                 .orElseThrow(() -> new EntityNotFoundException("Service not found."));

         if (faqRepository.existsByServiceAndQuestion(serviceId, question)) {
             throw new IllegalArgumentException("This service already has this question.");
         }

        Faq faq = new Faq();
        faq.setQuestion(question);
        faq.setAnswer(answer);
        faq.associateWithService(service);

        return faqRepository.save(faq);
    }

    public Faq updateFaq(UUID faqId, String question, String answer) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new EntityNotFoundException("Faq not found."));
        faq.setQuestion(question);
        faq.setAnswer(answer);
        return faqRepository.save(faq);
    }

    public void deleteFaq(UUID faqId) {
        if (!faqRepository.existsById(faqId)) {
            throw new EntityNotFoundException("Faq not found.");
        }
        faqRepository.deleteById(faqId);
    }
}
