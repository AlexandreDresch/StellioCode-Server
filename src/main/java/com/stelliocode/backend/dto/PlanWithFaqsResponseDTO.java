package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanWithFaqsResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal yearlyPrice;
    private String period;
    private List<String> features;
    private boolean isPopular;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FaqResponseDTO> faqs;
}