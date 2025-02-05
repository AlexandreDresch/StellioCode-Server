package com.stelliocode.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateFeaturedProjectRequest {
    private String title;
    private String description;
    private MultipartFile image;
}

