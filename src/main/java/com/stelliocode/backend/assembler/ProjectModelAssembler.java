package com.stelliocode.backend.assembler;

import com.stelliocode.backend.controller.AdminDashboardController;
import com.stelliocode.backend.dto.ProjectWithProgressResponseDTO;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.entity.ProjectProgress;
import com.stelliocode.backend.repository.ProjectProgressRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler implements RepresentationModelAssembler<Project, EntityModel<ProjectWithProgressResponseDTO>> {

    private final ProjectProgressRepository projectProgressRepository;

    public ProjectModelAssembler(ProjectProgressRepository projectProgressRepository) {
        this.projectProgressRepository = projectProgressRepository;
    }

    @Override
    public EntityModel<ProjectWithProgressResponseDTO> toModel(Project project) {
        Optional<ProjectProgress> latestProgress = projectProgressRepository.findTopByProjectIdOrderByUpdatedAtDesc(project.getId());

        ProjectWithProgressResponseDTO dto = new ProjectWithProgressResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getCreatedAt(),
                latestProgress.map(ProjectProgress::getProgressPercentage).orElse(BigDecimal.valueOf(0.0))
        );

        return EntityModel.of(dto,
                linkTo(methodOn(AdminDashboardController.class).getDeveloperProjects(project.getId(), 0, 10, null)).withSelfRel()
        );
    }
}

