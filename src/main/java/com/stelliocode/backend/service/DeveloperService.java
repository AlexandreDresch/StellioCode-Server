package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.DeveloperResponseDTO;
import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.ProjectRole;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.repository.DeveloperProjectRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeveloperService {

    private final UserRepository userRepository;
    private final DeveloperProjectRepository developerProjectRepository;
    private final ProjectRepository projectRepository;

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

    public boolean updateDeveloperStatus(UUID developerId, String status) {
        Optional<User> developerOpt = userRepository.findById(developerId);

        if (developerOpt.isPresent()) {
            User developer = developerOpt.get();

            developer.setStatus(status);
            if ("approved".equalsIgnoreCase(status)) {
                developer.setUpdatedAt(LocalDateTime.now());
            }

            userRepository.save(developer);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean removeDeveloper(UUID developerId) {
        Optional<User> developerOpt = userRepository.findById(developerId);

        if (developerOpt.isPresent()) {
            User developer = developerOpt.get();

            userRepository.delete(developer);
            return true;
        }

        return false;
    }

    @Transactional
    public List<DeveloperProject> assignDevelopersToProject(List<UUID> developerIds, UUID projectId, String roleInProject) {
        Project project = projectRepository.findById(String.valueOf(projectId))
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

    @Transactional
    public void removeDevelopersFromProject(List<UUID> developerIds, UUID projectId) {
        projectRepository.findById(String.valueOf(projectId))
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        for (UUID developerId : developerIds) {
            DeveloperProject association = developerProjectRepository.findByDeveloperIdAndProjectId(developerId, projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Association not found for developer: " + developerId));

            developerProjectRepository.delete(association);
        }
    }
}
