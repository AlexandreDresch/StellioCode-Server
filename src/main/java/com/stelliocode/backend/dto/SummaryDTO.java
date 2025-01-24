package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SummaryDTO {

    private Long totalUsers;
    private Long totalProjects;
    private Long completedProjects;
    private Long pendingProjects;
    private Double totalRevenue;
    private Integer newClients;
    private Integer newProjects;
}
