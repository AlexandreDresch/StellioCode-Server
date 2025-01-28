package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Faq;
import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.repository.FaqRepository;
import com.stelliocode.backend.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final PlanRepository planRepository;

    public Faq addFaq(UUID planId, String question, String answer) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found."));

        if (faqRepository.existsByPlanAndQuestion(plan, question)) {
            throw new IllegalArgumentException("This plan already has this question.");
        }

        Faq faq = new Faq();
        faq.setPlan(plan);
        faq.setQuestion(question);
        faq.setAnswer(answer);
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
