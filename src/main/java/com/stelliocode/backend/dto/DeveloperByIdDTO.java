package com.stelliocode.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeveloperByIdDTO {
    private UUID id;
    private String name;
    private String phone;
    private String status;
    private String level;
    private List<String> technologies;
}
