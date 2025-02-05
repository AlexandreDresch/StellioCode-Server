package com.stelliocode.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateFeaturedProjectRequestDTO {
    @NotBlank(message = "Title cannot be empty.")
    @Size(max = 255, message = "Title must be at most 255 characters long.")
    private String title;

    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @NotNull(message = "Image is required.")
    private MultipartFile image;
}
