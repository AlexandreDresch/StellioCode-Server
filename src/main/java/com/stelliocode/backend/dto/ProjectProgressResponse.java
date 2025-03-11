package com.stelliocode.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProjectProgressResponse {
    private String title;
    private BigDecimal progress;
    private List<String> descriptions;
    private List<String> imageUrls;
}