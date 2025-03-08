package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.FollowUpProjectResponseDTO;
import com.stelliocode.backend.dto.ProjectByIdDTO;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.service.ProjectService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{clientId}/{projectId}")
    public ResponseEntity<?> getProjectByIdClient(
            @PathVariable String clientId,
            @PathVariable UUID projectId) {

        Optional<Project> projectOpt = projectService.getProjectByIdAndClientGoogleId(projectId, clientId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            return ResponseEntity.ok(FollowUpProjectResponseDTO.fromProject(project));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
