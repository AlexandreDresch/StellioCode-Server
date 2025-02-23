package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal yearlyPrice;
    private String period;
    private List<String> features;
    private boolean isPopular;
}