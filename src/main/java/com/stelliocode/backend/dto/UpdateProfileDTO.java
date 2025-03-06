package com.stelliocode.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateProfileDTO {
    private String name;
    private String phone;
    private List<String> technologies;
}

