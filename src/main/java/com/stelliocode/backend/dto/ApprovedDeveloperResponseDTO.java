package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ApprovedDeveloperResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String avatarUrl;
    private String level;
    List<UUID> currentProjectIds;
    private int projectsCount;
    private String githubUrl;
    private List<String> techStack;
}
