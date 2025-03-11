package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.AddProgressRequest;
import com.stelliocode.backend.dto.ProjectProgressResponse;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.ProjectProgress;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.repository.DeveloperProjectRepository;
import com.stelliocode.backend.repository.ProjectProgressRepository;
import com.stelliocode.backend.repository.ProjectRepository;
import com.stelliocode.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectProgressService {

    private final ProjectProgressRepository projectProgressRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DeveloperProjectRepository developerProjectRepository;
    private final CloudinaryService cloudinaryService;

    public List<ProjectProgressResponse> getProgressByProjectId(UUID projectId) {
        List<ProjectProgress> progressList = projectProgressRepository.findByProjectIdOrderByCreatedAtAsc(projectId);
        return progressList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public boolean isProjectOwnedByClient(UUID projectId, String clientGoogleId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found."));

        return project.getClient().getGoogleId().equals(clientGoogleId);
    }

    public boolean isDeveloperAssignedToProject(UUID developerId, UUID projectId) {
        return developerProjectRepository.existsByDeveloperIdAndProjectId(developerId, projectId);
    }

    @Transactional
    public void addProgress(AddProgressRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found."));

        User developer = userRepository.findById(request.getDeveloperId())
                .orElseThrow(() -> new RuntimeException("Developer not found."));

        boolean isDeveloperAssigned = developerProjectRepository.existsByDeveloperAndProject(developer, project);
        if (!isDeveloperAssigned) {
            throw new RuntimeException("Developer is not assigned to project.");
        }

        String imageUrl = cloudinaryService.uploadImage(request.getImage());

        ProjectProgress progress = ProjectProgress.builder()
                .project(project)
                .progressPercentage(request.getProgressPercentage())
                .progressDescriptions(Collections.singletonList(request.getDescription()))
                .progressImages(Collections.singletonList(imageUrl))
                .updatedBy(developer)
                .build();

        projectProgressRepository.save(progress);
    }

    @Transactional
    public void deleteProgress(UUID progressId, UUID developerId) {
        ProjectProgress progress = projectProgressRepository.findById(progressId)
                .orElseThrow(() -> new RuntimeException("Progress data not found."));

        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found."));

        boolean isDeveloperAssigned = developerProjectRepository.existsByDeveloperAndProject(
                developer,
                progress.getProject()
        );
        if (!isDeveloperAssigned) {
            throw new RuntimeException("Developer is not part of the team.");
        }

        progress.getProgressImages().forEach(cloudinaryService::deleteImage);

        projectProgressRepository.delete(progress);
    }

    private ProjectProgressResponse mapToResponse(ProjectProgress progress) {
        return ProjectProgressResponse.builder()
                .title(progress.getCreatedAt().toString())
                .descriptions(progress.getProgressDescriptions())
                .imageUrls(progress.getProgressImages())
                .build();
    }
}