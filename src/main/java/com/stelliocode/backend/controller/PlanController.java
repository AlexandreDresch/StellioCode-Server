package com.stelliocode.backend.controller;

import com.stelliocode.backend.entity.Plan;
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
    public List<Plan> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanWithFaqs(@PathVariable UUID id) {
        Plan plan = planService.getPlanWithFaqs(id);
        return ResponseEntity.ok(plan);
    }
}
