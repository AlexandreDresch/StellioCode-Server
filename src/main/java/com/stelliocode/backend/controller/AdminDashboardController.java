package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.BaseResponseDTO;
import com.stelliocode.backend.dto.DeveloperResponseDTO;
import com.stelliocode.backend.dto.SummaryDTO;
import com.stelliocode.backend.entity.DeveloperProject;
import com.stelliocode.backend.service.DeveloperService;
import com.stelliocode.backend.service.SummaryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final SummaryService summaryService;
    private final DeveloperService developerService;

    public AdminDashboardController(SummaryService summaryService, DeveloperService developerService) {
        this.summaryService = summaryService;
        this.developerService = developerService;
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
            @RequestBody List<UUID> developerIds,
            @RequestParam(value = "roleInProject", required = false) String roleInProject
    ) {
        try {
            List<DeveloperProject> associations = developerService.assignDevelopersToProject(developerIds, projectId, roleInProject);
            return ResponseEntity.ok(new BaseResponseDTO("Developer(s) assigned to project successfully.", true));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(400).body(new BaseResponseDTO(ex.getMessage(), false));
        }
    }

    @DeleteMapping("/projects/{projectId}/developers")
    public ResponseEntity<BaseResponseDTO> removeDevelopersFromProject(
            @PathVariable("projectId") UUID projectId,
            @RequestBody List<UUID> developerIds
    ) {
        try {
            developerService.removeDevelopersFromProject(developerIds, projectId);
            return ResponseEntity.ok(new BaseResponseDTO("Developer(s) removed from project successfully.", true));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(new BaseResponseDTO(ex.getMessage(), false));
        }
    }
}