package com.stelliocode.backend.service;

import com.stelliocode.backend.assembler.ProjectModelAssembler;
import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.ProjectRole;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final UserRepository userRepository;
    private final DeveloperProjectRepository developerProjectRepository;
    private final ProjectRepository projectRepository;
    private final TechnologyRepository technologyRepository;
    private final UserTechnologyRepository userTechnologyRepository;
    private final ProjectModelAssembler projectModelAssembler;
    private final PagedResourcesAssembler<Project> pagedResourcesAssembler;


    public Page<DeveloperResponseDTO> getDevelopers(String name, String status, Pageable pageable) {
        String nameFilter = name == null ? "%" : "%" + name + "%";

        Page<User> developers = userRepository.findAllByRoleAndFilters(
                "developer",
                nameFilter,
                status,
                pageable
        );

        return developers.map(developer -> {
            long activeProjects = developerProjectRepository.countActiveProjectsByDeveloperId(developer.getId());
            return DeveloperResponseDTO.builder()
                    .id(developer.getId())
                    .fullName(developer.getFullName())
                    .status(developer.getStatus())
                    .level(developer.getLevel())
                    .activeProjects(activeProjects)
                    .build();
        });
    }

    public PagedModel<EntityModel<ProjectWithProgressResponseDTO>> getDeveloperProjects(UUID developerId, int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        Page<Project> projects;
        if (status != null && !status.isEmpty()) {
            projects = projectRepository.findByDeveloperIdAndStatus(developerId, status, pageable);
        } else {
            projects = projectRepository.findByDeveloperId(developerId, pageable);
        }

        return pagedResourcesAssembler.toModel(projects, projectModelAssembler);
    }

    public DeveloperByIdDTO getDeveloperById(UUID developerId) {
        return userRepository.findById(developerId)
                .map(developer -> {
                    List<String> technologies = userTechnologyRepository.findTechnologiesByDeveloperId(developerId);
                    return new DeveloperByIdDTO(
                            developer.getId(),
                            developer.getFullName(),
                            developer.getPhone(),
                            developer.getStatus(),
                            developer.getLevel(),
                            technologies
                    );
                })
                .orElse(null);
    }

    @Transactional
    public boolean updateDeveloper(UUID developerId, DeveloperUpdateDTO updateDTO) {
        var developerOptional = userRepository.findById(developerId);

        if (developerOptional.isEmpty()) {
            return false;
        }

        var developer = developerOptional.get();

        Optional.ofNullable(updateDTO.getName()).ifPresent(developer::setFullName);
        Optional.ofNullable(updateDTO.getPhone()).ifPresent(developer::setPhone);
        Optional.ofNullable(updateDTO.getStatus()).ifPresent(developer::setStatus);
        Optional.ofNullable(updateDTO.getLevel()).ifPresent(developer::setLevel);

        userRepository.save(developer);
        return true;
    }

    @Transactional
    public boolean removeDeveloper(UUID developerId) {
        Optional<User> developerOpt = userRepository.findById(developerId);

        if (developerOpt.isPresent()) {
            long count = developerProjectRepository.countByDeveloperId(developerId);
            if (count > 0) {
                throw new IllegalStateException("You cannot delete a developer with projects associations.");
            }

            userRepository.deleteById(developerId);
            return true;
        }

        return false;
    }

    @Transactional
    public List<DeveloperProject> assignDevelopersToProject(List<UUID> developerIds, UUID projectId, String roleInProject) {
        Project project = projectRepository.findById(UUID.fromString(String.valueOf(projectId)))
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        List<DeveloperProject> associations = new ArrayList<>();

        for (UUID developerId : developerIds) {
            User developer = userRepository.findById(developerId)
                    .orElseThrow(() -> new IllegalArgumentException("Developer not found: " + developerId));

            if (developerProjectRepository.findByDeveloperIdAndProjectId(developerId, projectId).isPresent()) {
                throw new IllegalStateException("Developer is already assigned to this project: " + developerId);
            }

            DeveloperProject developerProject = new DeveloperProject();
            developerProject.setDeveloper(developer);
            developerProject.setProject(project);
            developerProject.setRoleInProject(ProjectRole.valueOf(roleInProject));

            associations.add(developerProjectRepository.save(developerProject));
        }

        return associations;
    }

    public DeveloperStatsDTO getDeveloperStats() {
        long totalDevelopers = userRepository.countTotalDevelopers();
        List<Object[]> developersByStatusRaw = userRepository.countDevelopersByStatus();

        String[] possibleStatuses = {"pending", "approved", "rejected"};
        Map<String, Long> developersByStatus = new HashMap<>();

        for (String status : possibleStatuses) {
            developersByStatus.put(status, 0L);
        }

        for (Object[] row : developersByStatusRaw) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            developersByStatus.put(status, count);
        }

        return new DeveloperStatsDTO(totalDevelopers, developersByStatus);
    }

    public List<ApprovedDeveloperResponseDTO> getApprovedDevelopers() {
        List<User> developers = userRepository.findByRoleAndStatus("developer", "approved");

        return developers.stream().map(dev -> {
            List<UUID> currentProjectIds = projectRepository.findCurrentProjectIdsByDeveloperId(dev.getId());            int projectsCount = projectRepository.countByDeveloperId(dev.getId());
            List<String> techStack = technologyRepository.findTechnologiesByUserId(dev.getId());

            return new ApprovedDeveloperResponseDTO(
                    dev.getId(),
                    dev.getFullName(),
                    dev.getEmail(),
                    "https://i.pravatar.cc/150?img=" + dev.getId().hashCode() % 70,
                    dev.getLevel(),
                    currentProjectIds,
                    projectsCount,
                    "https://github.com/" + dev.getFullName().replaceAll(" ", "").toLowerCase(),
                    techStack
            );
        }).collect(Collectors.toList());
    }
}
