package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.FeaturedProjectResponse;
import com.stelliocode.backend.entity.FeaturedProject;
import com.stelliocode.backend.service.FeaturedProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/featured-projects")
@RequiredArgsConstructor
public class FeaturedProjectController {

    private final FeaturedProjectService featuredProjectService;

    @GetMapping
    public ResponseEntity<List<FeaturedProjectResponse>> getAllFeaturedProjects() {
        List<FeaturedProjectResponse> projects = featuredProjectService.getAllFeaturedProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeaturedProjectResponse> getFeaturedProjectById(@PathVariable("id") UUID projectId) {
        FeaturedProjectResponse featuredProject = featuredProjectService.getFeaturedProjectById(projectId);
        return ResponseEntity.ok(featuredProject);
    }
}

