package com.stelliocode.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
}
