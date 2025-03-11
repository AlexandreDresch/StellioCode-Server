package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequestDTO {
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String duration;
    private boolean isActive;
}
