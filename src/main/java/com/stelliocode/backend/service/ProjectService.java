package com.stelliocode.backend.service;

import com.stelliocode.backend.entity.Client;
import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.ProjectStatus;
import com.stelliocode.backend.repository.PlanRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PlanRepository planRepository;

    public Project createProject(String title, String description, Double price, Client client, UUID planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setPrice(BigDecimal.valueOf(price));
        project.setStatus(ProjectStatus.PENDING);
        project.setClient(client);
        project.setPlan(plan);
        return projectRepository.save(project);
    }
}
