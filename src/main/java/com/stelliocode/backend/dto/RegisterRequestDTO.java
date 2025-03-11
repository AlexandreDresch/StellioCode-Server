package com.stelliocode.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Full name cannot be blank.")
    private String fullName;

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotBlank(message = "Phone number cannot be blank.")
    private String phone;

    @NotBlank(message = "Role cannot be blank.")
    @Pattern(regexp = "ADMIN|DEVELOPER", message = "Role must be 'ADMIN' or 'DEVELOPER'.")
    private String role;

    @NotBlank(message = "Level cannot be blank.")
    @Pattern(regexp = "junior|mid_level|senior", message = "Level must be 'junior', 'mid_level' or 'senior'.")
    private String level;

    @NotEmpty(message = "Technologies list cannot be empty.")
    private List<String> technologies;
}
