package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.ProjectStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateProjectDTO {
    private String title;
    private String description;
    private ProjectStatus status;
    private BigDecimal price;
    private UUID planId;
    private UUID serviceId;
}