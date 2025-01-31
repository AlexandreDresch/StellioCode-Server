package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.entity.Faq;
import com.stelliocode.backend.entity.Plan;
import com.stelliocode.backend.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final SummaryService summaryService;
    private final DeveloperService developerService;
    private final PlanService planService;
    private final FaqService faqService;
    private final ProjectService projectService;


    public AdminDashboardController(SummaryService summaryService, DeveloperService developerService, PlanService planService, FaqService faqService, ProjectService projectService) {
        this.summaryService = summaryService;
        this.developerService = developerService;
        this.planService = planService;
        this.faqService = faqService;
        this.projectService = projectService;
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> getSummary() {
        SummaryDTO summary = summaryService.getSummary();
        return ResponseEntity.ok(summary);
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

        pagedModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdminDashboardController.class)
                        .getDevelopers(name, status, page, size, assembler))
                .withSelfRel());

        return pagedModel;
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

    @PatchMapping("/developers/{id}/approve")
    public ResponseEntity<BaseResponseDTO> approveDeveloper(
            @PathVariable("id") UUID developerId,
            @RequestParam("status") String status
    ) {
        boolean updated = developerService.updateDeveloperStatus(developerId, status);

        if (updated) {
            return ResponseEntity.ok(new BaseResponseDTO("Developer status updated successfully.", true));
        } else {
            return ResponseEntity.status(400).body(new BaseResponseDTO("Failed to update developer status.", false));
        }
    }

    @DeleteMapping("/developers/{developerId}")
    public ResponseEntity<BaseResponseDTO> deleteDeveloper(
            @PathVariable("developerId") UUID developerId
    ) {
        boolean removed = developerService.removeDeveloper(developerId);

        if (removed) {
            return ResponseEntity.ok(new BaseResponseDTO("Developer removed successfully.", true));
        } else {
            return ResponseEntity.status(404).body(new BaseResponseDTO("Developer not found.", false));
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

    @DeleteMapping("/projects/{projectId}/developers")
    public ResponseEntity<BaseResponseDTO> removeDevelopersFromProject(
            @PathVariable("projectId") UUID projectId,
            @RequestBody DeveloperAssignmentRemovalRequestDTO request
    ) {
        try {
            developerService.removeDevelopersFromProject(request.getDeveloperIds(), projectId);
            return ResponseEntity.ok(new BaseResponseDTO("Developer(s) removed from project successfully.", true));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(new BaseResponseDTO(ex.getMessage(), false));
        }
    }

    @PostMapping("/plans")
    @ResponseStatus(HttpStatus.CREATED)
    public Plan createPlan(@RequestBody Plan plan) {
        return planService.createPlan(plan);
    }

    @PutMapping("/plans/{id}")
    public Plan updatePlan(@PathVariable UUID id, @RequestBody Plan updatedPlan) {
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
        Faq faq = faqService.addFaq(faqRequest.getPlanId(), faqRequest.getQuestion(), faqRequest.getAnswer());
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
    public ResponseEntity<List<InternalProjectDetailsResponseDTO>> getAllProjects() {
        List<InternalProjectDetailsResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
}