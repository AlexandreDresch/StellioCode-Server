package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.service.DeveloperProjectService;
import com.stelliocode.backend.service.DeveloperService;
import com.stelliocode.backend.service.MeetingService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/developer/dashboard")
public class DeveloperDashboardController {

    private final DeveloperProjectService developerProjectService;
    private final MeetingService meetingService;
    private final DeveloperService developerService;

    public DeveloperDashboardController(DeveloperProjectService developerProjectService, MeetingService meetingService, DeveloperService developerService) {
        this.developerProjectService = developerProjectService;
        this.meetingService = meetingService;
        this.developerService = developerService;
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
}
