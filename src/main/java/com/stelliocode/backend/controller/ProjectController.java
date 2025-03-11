package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.FollowUpProjectResponseDTO;
import com.stelliocode.backend.dto.ProjectProgressResponse;
import com.stelliocode.backend.entity.Payment;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.service.PaymentService;
import com.stelliocode.backend.service.ProjectProgressService;
import com.stelliocode.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final PaymentService paymentService;
    private final ProjectProgressService projectProgressService;

    @GetMapping("/{clientId}/{projectId}")
    public ResponseEntity<?> getProjectByIdClient(
            @PathVariable String clientId,
            @PathVariable UUID projectId) {

        Optional<Project> projectOpt = projectService.getProjectByIdAndClientGoogleId(projectId, clientId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            Optional<Payment> paymentOpt = paymentService.getPaymentByProjectId(projectId);

            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();

                FollowUpProjectResponseDTO response = FollowUpProjectResponseDTO.fromProject(project, payment);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{clientId}/progress/{projectId}")
    public ResponseEntity<List<ProjectProgressResponse>> getProgressByProjectId(@PathVariable String clientId, @PathVariable UUID projectId) {

        if (!projectProgressService.isProjectOwnedByClient(projectId, clientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ProjectProgressResponse> progressList = projectProgressService.getProgressByProjectId(projectId);
        return ResponseEntity.ok(progressList);
    }
}
