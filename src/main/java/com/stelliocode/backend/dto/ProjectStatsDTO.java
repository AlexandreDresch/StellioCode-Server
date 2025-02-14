package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatsDTO {
    private String month;
    private Long inProgress;
    private Long completed;
}
