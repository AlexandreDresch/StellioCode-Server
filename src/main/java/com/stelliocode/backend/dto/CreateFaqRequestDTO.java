package com.stelliocode.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateFaqRequestDTO {
    @NotNull(message = "Plan ID cannot be null.")
    private UUID planId;

    @NotBlank(message = "Question cannot be blank.")
    @Size(min = 5, max = 200, message = "Question must be between 5 and 200 characters.")
    private String question;

    @NotBlank(message = "Answer cannot be blank.")
    @Size(min = 10, max = 1000, message = "Answer must be between 10 and 1000 characters.")
    private String answer;
}
