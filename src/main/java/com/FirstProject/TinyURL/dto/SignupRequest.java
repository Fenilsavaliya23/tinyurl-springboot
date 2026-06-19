package com.FirstProject.TinyURL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(

        @NotBlank(message = "Username is required")
        String username,

        @Email(message = "Invalid Email Format")
        @NotBlank(message = "Email id is required")
        String email,

        @NotBlank(message = "Password is required")
        String password
)
{}
