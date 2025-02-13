package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateProjectStatusDTO(
        @NotNull(message = "Status cannot be null.")
        ProjectStatus status
) {}
