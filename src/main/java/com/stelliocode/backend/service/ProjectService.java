package com.stelliocode.backend.service;

import com.stelliocode.backend.controller.AdminDashboardController;
import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.repository.DeveloperProjectRepository;
import com.stelliocode.backend.repository.PlanRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PlanRepository planRepository;
    private final DeveloperProjectRepository developerProjectRepository;
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

    @Transactional
    public UpdateProjectStatusResponseDTO updateProjectStatus(UUID projectId, ProjectStatus newStatus) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found."));

        project.setStatus(newStatus);

        projectRepository.save(project);

        return new UpdateProjectStatusResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getStatus().toString()
        );
    }

    public PagedModel<InternalProjectDetailsResponseDTO> getAllProjectsWithDevelopers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projectsPage = projectRepository.findAll(pageable);

        List<InternalProjectDetailsResponseDTO> projects = projectsPage.getContent().stream()
                .map(project -> {
                    List<DeveloperProject> developerProjects = developerProjectRepository.findByProjectId(project.getId());

                    List<DeveloperDTO> developers = developerProjects.stream()
                            .map(dp -> {
                                User developer = dp.getDeveloper();
                                return new DeveloperDTO(
                                        developer.getId().toString(),
                                        developer.getFullName(),
                                        dp.getRoleInProject().toString()
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

    public List<ProjectStatsDTO> getProjectsStatsLast6Months() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(5).withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        List<String> last6Months = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            last6Months.add(startDate.plusMonths(i).format(formatter));
        }

        List<Object[]> rawData = projectRepository.getProjectsStatsLast6Months(startDate.atStartOfDay());

        Map<String, ProjectStatsDTO> statsMap = rawData.stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> new ProjectStatsDTO(
                                (String) obj[0],
                                ((Number) obj[1]).longValue(),
                                ((Number) obj[2]).longValue()
                        )
                ));

        return last6Months.stream()
                .map(month -> statsMap.getOrDefault(month, new ProjectStatsDTO(month, 0L, 0L)))
                .collect(Collectors.toList());
    }

    public ProjectByIdDTO getProjectById(UUID projectId) {
        Optional<Project> project = projectRepository.findById(projectId);

        if (!project.isPresent()) {
            throw new EntityNotFoundException("Project not found.");
        }

        Project foundProject = project.get();

        return new ProjectByIdDTO(
                foundProject.getId(),
                foundProject.getTitle(),
                foundProject.getDescription(),
                foundProject.getStatus().toString(),
                foundProject.getPrice(),
                foundProject.getPlan() != null ? foundProject.getPlan().getName() : null,
                foundProject.getService() != null ? foundProject.getService().getTitle() : null,
                foundProject.getClient() != null ? foundProject.getClient().getName() : null
        );
    }

    @Transactional
    public void removeDeveloperFromProject(UUID projectId, UUID developerId) {
        var developerProject = developerProjectRepository.findByDeveloperIdAndProjectId(developerId, projectId)
                .orElseThrow(() -> new IllegalArgumentException("Developer is not assigned to this project."));

        developerProjectRepository.delete(developerProject);
    }
}
