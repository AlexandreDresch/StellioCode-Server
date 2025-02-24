package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.*;
import com.stelliocode.backend.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final SummaryService summaryService;
    private final DeveloperService developerService;
    private final PlanService planService;
    private final FaqService faqService;
    private final ProjectService projectService;
    private final FeaturedProjectService featuredProjectService;
    private final AuthenticationService authenticationService;
    private final ServiceService serviceService;
    private final MeetingService meetingService;


    public AdminDashboardController(SummaryService summaryService, DeveloperService developerService, PlanService planService, FaqService faqService, ProjectService projectService, FeaturedProjectService featuredProjectService, AuthenticationService authenticationService, ServiceService serviceService, MeetingService meetingService) {
        this.summaryService = summaryService;
        this.developerService = developerService;
        this.planService = planService;
        this.faqService = faqService;
        this.projectService = projectService;
        this.featuredProjectService = featuredProjectService;
        this.authenticationService = authenticationService;
        this.serviceService = serviceService;
        this.meetingService = meetingService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> getDashboardSummary() {
        return ResponseEntity.ok(summaryService.getSummary());
    }

    @GetMapping("/developers")
    public PagedModel<EntityModel<DeveloperResponseDTO>> getDevelopers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            PagedResourcesAssembler<DeveloperResponseDTO> assembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DeveloperResponseDTO> developers = developerService.getDevelopers(name, status, pageable);

        PagedModel<EntityModel<DeveloperResponseDTO>> pagedModel = assembler.toModel(developers);

        pagedModel.add(linkTo(methodOn(AdminDashboardController.class)
                        .getDevelopers(name, status, page, size, assembler))
                .withSelfRel());

        return pagedModel;
    }

    @GetMapping("/developers/{id}")
    public ResponseEntity<?> getDeveloperById(@PathVariable("id") UUID developerId) {
        DeveloperByIdDTO developer = developerService.getDeveloperById(developerId);

        if (developer != null) {
            return ResponseEntity.ok(developer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/developers/{developerId}/projects")
    public ResponseEntity<PagedModel<EntityModel<ProjectWithProgressResponseDTO>>> getDeveloperProjects(
            @PathVariable UUID developerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        PagedModel<EntityModel<ProjectWithProgressResponseDTO>> projects = developerService.getDeveloperProjects(developerId, page, size, status);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/developers/stats")
    public DeveloperStatsDTO getDeveloperStats() {
        return developerService.getDeveloperStats();
    }

    @PatchMapping("/developers/{id}")
    public ResponseEntity<BaseResponseDTO> updateDeveloper(
            @PathVariable("id") UUID developerId,
            @RequestBody DeveloperUpdateDTO updateDTO
    ) {
        boolean updated = developerService.updateDeveloper(developerId, updateDTO);

        if (updated) {
            return ResponseEntity.ok(new BaseResponseDTO("Developer updated successfully.", true));
        } else {
            return ResponseEntity.badRequest().body(new BaseResponseDTO("Failed to update developer.", false));
        }
    }

    @GetMapping("/developers/approved")
    public ResponseEntity<List<ApprovedDeveloperResponseDTO>> getApprovedDevelopers() {
        List<ApprovedDeveloperResponseDTO> developers = developerService.getApprovedDevelopers();
        return ResponseEntity.ok(developers);
    }

    @DeleteMapping("/developers/{developerId}")
    public ResponseEntity<BaseResponseDTO> deleteDeveloper(@PathVariable("developerId") UUID developerId) {
        try {
            boolean removed = developerService.removeDeveloper(developerId);
            return ResponseEntity.ok(new BaseResponseDTO("Developer removed successfully.", true));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponseDTO("Cannot delete developer: " + e.getMessage(), false));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseDTO("Developer not found.", false));
        }
    }

    @PostMapping("/projects/{projectId}/developers")
    public ResponseEntity<BaseResponseDTO> assignDevelopersToProject(
            @PathVariable("projectId") UUID projectId,
            @RequestBody DeveloperAssignmentRemovalRequestDTO request,
            @RequestParam(value = "roleInProject", required = false) String roleInProject
    ) {
        try {
            List<DeveloperProject> associations = developerService.assignDevelopersToProject(request.getDeveloperIds(), projectId, roleInProject);
            return ResponseEntity.ok(new BaseResponseDTO("Developer(s) assigned to project successfully.", true));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(400).body(new BaseResponseDTO(ex.getMessage(), false));
        }
    }

    @PutMapping("/projects/{projectId}/status")
    public ResponseEntity<UpdateProjectStatusResponseDTO> updateProjectStatus(
            @PathVariable UUID projectId,
            @RequestBody @Valid UpdateProjectStatusDTO request
    ) {
        UpdateProjectStatusResponseDTO updatedProject = projectService.updateProjectStatus(projectId, request.status());
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("/projects/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable UUID projectId,
            @Valid @RequestBody UpdateProjectDTO updateProjectDTO) {
        Project updatedProject = projectService.updateProject(projectId, updateProjectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{projectId}/developers/{developerId}")
    public ResponseEntity<String> removeDeveloperFromProject(
            @PathVariable UUID projectId,
            @PathVariable UUID developerId
    ) {
        projectService.removeDeveloperFromProject(projectId, developerId);
        return ResponseEntity.ok("Developer removed from project successfully");
    }

    @GetMapping("/meetings")
    public CollectionModel<EntityModel<MeetingResponseDTO>> getAllMeetings(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime scheduledAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<MeetingResponseDTO> meetings = meetingService.getAllMeetings(status, scheduledAt, page, size);

        List<EntityModel<MeetingResponseDTO>> meetingModels = meetings.getContent().stream()
                .map(meeting -> EntityModel.of(meeting,
                        linkTo(methodOn(AdminDashboardController.class)
                                .getAllMeetings(status, scheduledAt, page, size)).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(meetingModels,
                linkTo(methodOn(AdminDashboardController.class)
                        .getAllMeetings(status, scheduledAt, page, size)).withSelfRel()
        );
    }

    @PutMapping("/meetings/{id}/status")
    public ResponseEntity<UpdatedMeetingResponseDTO> updateMeetingStatus(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMeetingStatusDTO request
    ) {
        UpdatedMeetingResponseDTO updatedMeeting = meetingService.updateMeetingStatus(id, request.status());
        return ResponseEntity.ok(updatedMeeting);
    }

    @PutMapping("/meetings/{id}/date")
    public ResponseEntity<UpdatedMeetingResponseDTO> updateMeetingDate(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMeetingDateDTO request
    ) {
        UpdatedMeetingResponseDTO updatedMeeting = meetingService.updateMeetingDate(id, request.date());
        return ResponseEntity.ok(updatedMeeting);
    }

    @PostMapping("/plans")
    @ResponseStatus(HttpStatus.CREATED)
    public PlanResponseDTO createPlan(@RequestBody CreatePlanRequestDTO plan) {
        return planService.createPlan(plan);
    }

    @PutMapping("/plans/{id}")
    public PlanResponseDTO updatePlan(@PathVariable UUID id, @RequestBody CreatePlanRequestDTO updatedPlan) {
        return planService.updatePlan(id, updatedPlan);
    }

    @DeleteMapping("/plans/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable UUID id) {
        planService.deletePlan(id);
    }

    @GetMapping("/plans/stats")
    public ResponseEntity<List<Map<String, Object>>> getPlanStatistics() {
        List<Map<String, Object>> stats = planService.getPlanStatistics();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/plans/faq")
    public ResponseEntity<Faq> addFaq(@RequestBody CreateFaqRequestDTO faqRequest) {
        Faq faq = faqService.addFaqToPlan(faqRequest.getPlanId(), faqRequest.getQuestion(), faqRequest.getAnswer());
        return ResponseEntity.ok(faq);
    }

    @PutMapping("/plans/faq/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable UUID id,
                                         @RequestBody UpdateFaqRequestDTO faqRequest) {
        Faq faq = faqService.updateFaq(id, faqRequest.getQuestion(), faqRequest.getAnswer());
        return ResponseEntity.ok(faq);
    }

    @DeleteMapping("/plans/faq/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable UUID id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/projects")
    public ResponseEntity<PagedModel<InternalProjectDetailsResponseDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedModel<InternalProjectDetailsResponseDTO> projects = projectService.getAllProjectsWithDevelopers(page, size);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/projects/stats/last-6-months")
    public List<ProjectStatsDTO> getProjectsStatsLast6Months() {
        return projectService.getProjectsStatsLast6Months();
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") UUID projectId) {
        ProjectByIdDTO project = projectService.getProjectById(projectId);

        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/featured-projects")
    public ResponseEntity<FeaturedProjectDTO> createFeaturedProject(
            @Valid @ModelAttribute CreateFeaturedProjectRequestDTO requestDTO,
            Principal principal) {

        UUID userId = authenticationService.getUserIdFromPrincipal(principal.getName());
        return ResponseEntity.ok(featuredProjectService.createFeaturedProject(userId, requestDTO.getTitle(), requestDTO.getDescription(), requestDTO.getImage()));
    }

    @PutMapping("/featured-projects/{id}")
    public ResponseEntity<FeaturedProjectDTO> updateFeaturedProject(
            @PathVariable("id") UUID projectId,
            @ModelAttribute UpdateFeaturedProjectRequest request) {

        return ResponseEntity.ok(featuredProjectService.updateFeaturedProject(projectId, request));
    }

    @DeleteMapping("/featured-projects/{id}")
    public ResponseEntity<Void> deleteFeaturedProject(@PathVariable("id") UUID projectId) {
        featuredProjectService.deleteFeaturedProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceResponseDTO> createService(@Valid @RequestBody CreateServiceRequestDTO request) {
        return ResponseEntity.ok(serviceService.createService(request));
    }

    @PostMapping("/services/faq")
    public ResponseEntity<Faq> addFaqToService(@RequestBody CreateFaqRequestDTO faqRequest) {
        Faq faq = faqService.addFaqToService(faqRequest.getPlanId(), faqRequest.getQuestion(), faqRequest.getAnswer());
        return ResponseEntity.ok(faq);
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceResponseDTO> updateService(@PathVariable UUID id, @Valid @RequestBody CreateServiceRequestDTO request) {
        return ResponseEntity.ok(serviceService.updateService(id, request));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}