package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeveloperDTO {
    private String developerId;
    private String developerName;
    private String roleInProject;
}