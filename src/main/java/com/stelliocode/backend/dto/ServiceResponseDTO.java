package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String duration;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}