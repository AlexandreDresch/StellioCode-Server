package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.DeveloperDTO;
import com.stelliocode.backend.dto.DeveloperProjectResponseDTO;
import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.repository.DeveloperProjectRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeveloperProjectService {

    private final DeveloperProjectRepository developerProjectRepository;
    private final ProjectRepository projectRepository;

    public DeveloperProjectService(DeveloperProjectRepository developerProjectRepository, ProjectRepository projectRepository) {
        this.developerProjectRepository = developerProjectRepository;
        this.projectRepository = projectRepository;
    }

    public PagedModel<DeveloperProjectResponseDTO> getProjectsByDeveloper(UUID developerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DeveloperProject> developerProjectsPage = developerProjectRepository.findByDeveloperId(developerId, pageable);

        List<DeveloperProjectResponseDTO> projects = developerProjectsPage.getContent().stream()
                .map(devProject -> {
                    Project project = devProject.getProject();

                    List<DeveloperDTO> developers = developerProjectRepository.findByProjectId(project.getId()).stream()
                            .map(dp -> new DeveloperDTO(
                                    dp.getDeveloper().getId().toString(),
                                    dp.getDeveloper().getFullName(),
                                    dp.getRoleInProject().toString()
                            ))
                            .collect(Collectors.toList());

                    return new DeveloperProjectResponseDTO(
                            project.getId().toString(),
                            project.getTitle(),
                            project.getStatus().toString(),
                            project.getId().toString(),
                            project.getPrice(),
                            project.getClient().getName(),
                            developers
                    );
                })
                .collect(Collectors.toList());

        return PagedModel.of(
                projects,
                new PagedModel.PageMetadata(
                        developerProjectsPage.getSize(),
                        developerProjectsPage.getNumber(),
                        developerProjectsPage.getTotalElements(),
                        developerProjectsPage.getTotalPages()
                )
        );
    }
}
