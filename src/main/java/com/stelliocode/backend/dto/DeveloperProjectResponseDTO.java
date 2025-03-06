package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeveloperProjectResponseDTO {
    private String id;
    private String title;
    private String status;
    private String projectId;
    private BigDecimal price;
    private String clientName;
    private List<DeveloperDTO> developers;
}
