package com.FirstProject.TinyURL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Invalid Email format")
        @NotBlank(message = "email is is required")
        String email,

        @NotBlank(message = "password is required")
        String password
) {
}
