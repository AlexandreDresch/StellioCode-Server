package com.stelliocode.backend.service;

import com.stelliocode.backend.dto.FeaturedProjectDTO;
import com.stelliocode.backend.dto.FeaturedProjectResponse;
import com.stelliocode.backend.dto.UpdateFeaturedProjectRequest;
import com.stelliocode.backend.entity.FeaturedProject;
import com.stelliocode.backend.entity.User;
import com.stelliocode.backend.exception.NotFoundException;
import com.stelliocode.backend.repository.FeaturedProjectRepository;
import com.stelliocode.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeaturedProjectService {

    private final FeaturedProjectRepository featuredProjectRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public List<FeaturedProjectResponse> getAllFeaturedProjects() {
        List<FeaturedProject> projects = featuredProjectRepository.findAll();

        return projects.stream()
                .map(project -> new FeaturedProjectResponse(
                        project.getId(),
                        project.getTitle(),
                        project.getDescription(),
                        project.getImageUrl(),
                        project.getCreatedAt(),
                        project.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    public FeaturedProjectDTO createFeaturedProject(UUID userId, String title, String description, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Developer not found."));

        String imageUrl = cloudinaryService.uploadImage(image);

        FeaturedProject project = FeaturedProject.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        FeaturedProject savedProject = featuredProjectRepository.save(project);
        return mapToDTO(savedProject);
    }

    public FeaturedProjectResponse getFeaturedProjectById(UUID projectId) {
        FeaturedProject project =  featuredProjectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Featured Project not found with id: " + projectId));

        return new FeaturedProjectResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getImageUrl(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }

    public FeaturedProjectDTO updateFeaturedProject(UUID projectId, UpdateFeaturedProjectRequest request) {
        FeaturedProject project = featuredProjectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found."));

        if (request.getTitle() != null) {
            project.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getImage() != null) {
            String imageUrl = cloudinaryService.uploadImage(request.getImage());
            project.setImageUrl(imageUrl);
        }

        FeaturedProject updatedProject = featuredProjectRepository.save(project);

        return new FeaturedProjectDTO(updatedProject.getId(), updatedProject.getTitle(),
                updatedProject.getDescription(), updatedProject.getImageUrl(), updatedProject.getCreatedBy().getId(), updatedProject.getCreatedAt(), updatedProject.getUpdatedAt() );
    }

    public void deleteFeaturedProject(UUID projectId) {
        FeaturedProject project = featuredProjectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found."));

        cloudinaryService.deleteImage(project.getImageUrl());
        featuredProjectRepository.delete(project);
    }

    private FeaturedProjectDTO mapToDTO(FeaturedProject project) {
        return FeaturedProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .imageUrl(project.getImageUrl())
                .createdBy(project.getCreatedBy().getId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
