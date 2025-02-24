package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProjectByIdDTO {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private BigDecimal price;
    private String planName;
    private String serviceName;
    private String clientName;
}





