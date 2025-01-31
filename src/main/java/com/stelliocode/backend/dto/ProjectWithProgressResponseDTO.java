package com.stelliocode.backend.dto;

import com.stelliocode.backend.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProjectWithProgressResponseDTO {
    private UUID id;
    private String title;
    private ProjectStatus status;
    private LocalDateTime startDate;
    private BigDecimal progressPercentage;
}
