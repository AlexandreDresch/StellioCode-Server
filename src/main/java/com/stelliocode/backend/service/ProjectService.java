package com.stelliocode.backend.service;

import com.stelliocode.backend.controller.AdminDashboardController;
import com.stelliocode.backend.dto.DeveloperDTO;
import com.stelliocode.backend.dto.InternalProjectDetailsResponseDTO;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.DeveloperProjectsRepository;
import com.stelliocode.backend.repository.PlanRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    private final ServiceRepository serviceRepository;

    public Project createProject(String title, String description, Double price, Client client, UUID planId, UUID serviceId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found."));
        com.stelliocode.backend.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found."));
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setPrice(BigDecimal.valueOf(price));
        project.setStatus(ProjectStatus.PENDING);
        project.setClient(client);
        project.setPlan(plan);
        project.setService(service);
        return projectRepository.save(project);
    }

    public PagedModel<InternalProjectDetailsResponseDTO> getAllProjectsWithDevelopers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projectsPage = projectRepository.findAll(pageable);

        List<InternalProjectDetailsResponseDTO> projects = projectsPage.getContent().stream()
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
                            project.getService().getTitle(),
                            project.getClient().getId().toString(),
                            project.getClient().getName(),
                            project.getClient().getEmail(),
                            project.getClient().getPhone(),
                            project.getClient().getProfilePicture(),
                            developers
                    );
                })
                .collect(Collectors.toList());

        return PagedModel.of(
                projects,
                new PagedModel.PageMetadata(
                        projectsPage.getSize(),
                        projectsPage.getNumber(),
                        projectsPage.getTotalElements(),
                        projectsPage.getTotalPages()
                ),
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(AdminDashboardController.class).getAllProjects(page, size)
                ).withSelfRel()
        );
    }
}
