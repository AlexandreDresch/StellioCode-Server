package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProgressRequest {

    private UUID projectId;
    private BigDecimal progressPercentage;
    private String description;
    private MultipartFile image;
    private UUID developerId;
}