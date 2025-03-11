package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.Payment;
import com.stelliocode.backend.entity.Project;
import com.stelliocode.backend.service.*;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/developer/dashboard")
public class DeveloperDashboardController {

    private final DeveloperProjectService developerProjectService;
    private final MeetingService meetingService;
    private final DeveloperService developerService;
    private final ProjectProgressService projectProgressService;
    private final PaymentService paymentService;
    private final ProjectService projectService;

    public DeveloperDashboardController(DeveloperProjectService developerProjectService, MeetingService meetingService, DeveloperService developerService, ProjectProgressService projectProgressService, PaymentService paymentService, ProjectService projectService) {
        this.developerProjectService = developerProjectService;
        this.meetingService = meetingService;
        this.developerService = developerService;
        this.projectProgressService = projectProgressService;
        this.paymentService = paymentService;
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public ResponseEntity<PagedModel<DeveloperProjectResponseDTO>> getDeveloperProjects(
            @RequestParam(required = false) String developerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        if (developerId == null) {
            developerId = principal.getName();
        }

        PagedModel<DeveloperProjectResponseDTO> projects = developerProjectService.getProjectsByDeveloper(UUID.fromString(developerId), page, size);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects/{developerId}/{projectId}")
    public ResponseEntity<?> getProjectById(
            @PathVariable UUID developerId,
            @PathVariable UUID projectId) {

        Optional<Project> projectOpt = projectService.getProjectByIdAndDeveloperId(projectId, developerId);

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

    @GetMapping("/{developerId}/projects/{projectId}/follow-up")
    public ResponseEntity<List<ProjectProgressResponse>> getProgressByProjectId(@PathVariable UUID developerId, @PathVariable UUID projectId) {

        if (!projectProgressService.isDeveloperAssignedToProject(developerId, projectId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ProjectProgressResponse> progressList = projectProgressService.getProgressByProjectId(projectId);
        return ResponseEntity.ok(progressList);
    }

    @GetMapping("/{developerId}/projects/{projectId}/payment")
    public ResponseEntity<?> getPaymentById(
            @PathVariable UUID developerId,
            @PathVariable UUID projectId) {

        PaymentResponse response = paymentService.getPaymentByIdAndDeveloperId(projectId, developerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meetings")
    public CollectionModel<EntityModel<MeetingResponseDTO>> getDeveloperMeetings(
            @RequestParam UUID developerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingResponseDTO> meetings = meetingService.getMeetingsByDeveloper(developerId, page, size);

        List<EntityModel<MeetingResponseDTO>> meetingModels = meetings.getContent().stream()
                .map(meeting -> EntityModel.of(meeting,
                        linkTo(methodOn(DeveloperDashboardController.class)
                                .getDeveloperMeetings(developerId, page, size)).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(meetingModels,
                linkTo(methodOn(DeveloperDashboardController.class)
                        .getDeveloperMeetings(developerId, page, size)).withSelfRel()
        );
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable("id") UUID developerId) {
        DeveloperByIdDTO developer = developerService.getDeveloperById(developerId);

        if (developer != null) {
            return ResponseEntity.ok(developer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/profile/{id}")
    public ResponseEntity<BaseResponseDTO> updateDeveloper(
            @PathVariable("id") UUID developerId,
            @RequestBody UpdateProfileDTO updateDTO
    ) {
        boolean updated = developerService.updateProfile(developerId, updateDTO);

        if (updated) {
            return ResponseEntity.ok(new BaseResponseDTO("Profile updated successfully.", true));
        } else {
            return ResponseEntity.badRequest().body(new BaseResponseDTO("Failed to update profile.", false));
        }
    }

    @PostMapping(value = "/project-progress", consumes = "multipart/form-data")
    public ResponseEntity<String> addProgress(
            @RequestPart("projectId") String projectId,
            @RequestPart("progressPercentage") String progressPercentage,
            @RequestPart("description") String description,
            @RequestPart("image") MultipartFile image,
            @RequestPart("developerId") String developerId) {

        AddProgressRequest request = AddProgressRequest.builder()
                .projectId(UUID.fromString(projectId))
                .progressPercentage(new BigDecimal(progressPercentage))
                .description(description)
                .image(image)
                .developerId(UUID.fromString(developerId))
                .build();

        try {
            projectProgressService.addProgress(request);
            return ResponseEntity.ok("Progress added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while adding progress: " + e.getMessage());
        }
    }

    @DeleteMapping("/project-progress/{progressId}")
    public ResponseEntity<String> deleteProgress(
            @PathVariable UUID progressId,
            @RequestParam UUID developerId
    ) {
        try {
            projectProgressService.deleteProgress(progressId, developerId);
            return ResponseEntity.ok("Progress deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while deleting progress: " + e.getMessage());
        }
    }
}
