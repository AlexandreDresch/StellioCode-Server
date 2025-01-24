package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.DeveloperResponseDTO;
import com.stelliocode.backend.dto.SummaryDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}