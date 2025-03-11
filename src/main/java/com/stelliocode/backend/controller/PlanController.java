package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.PlanResponseDTO;
import com.stelliocode.backend.dto.PlanWithFaqsResponseDTO;
import com.stelliocode.backend.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public List<PlanResponseDTO> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanWithFaqsResponseDTO> getPlanWithFaqs(@PathVariable UUID id) {
        PlanWithFaqsResponseDTO plan = planService.getPlanWithFaqs(id);
        return ResponseEntity.ok(plan);
    }
}
