package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.DeveloperDTO;
import com.stelliocode.backend.dto.InternalProjectDetailsResponseDTO;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.DeveloperProjectsRepository;
import com.stelliocode.backend.repository.PlanRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PlanRepository planRepository;
    private final DeveloperProjectsRepository developerProjectsRepository;

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

    public List<InternalProjectDetailsResponseDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(project -> {
                    List<DeveloperProjects> developerProjects = developerProjectsRepository.findByProjectId(project.getId());

                    List<DeveloperDTO> developers = developerProjects.stream()
                            .map(dp -> {
                                User developer = dp.getDeveloper();

                                return new DeveloperDTO(
                                        developer.getId().toString(),
                                        developer.getFullName(),
                                        dp.getRoleInProject()
                                );
                            })
                            .collect(Collectors.toList());

                    return new InternalProjectDetailsResponseDTO(
                            project.getId().toString(),
                            project.getTitle(),
                            project.getDescription(),
                            project.getStatus().toString(),
                            project.getPrice(),
                            project.getPlan().getName(),
                            project.getClient().getId().toString(),
                            project.getClient().getName(),
                            project.getClient().getEmail(),
                            project.getClient().getPhone(),
                            project.getClient().getProfilePicture(),
                            developers
                    );
                })
                .collect(Collectors.toList());
    }


}
