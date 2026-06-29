package com.FirstProject.TinyURL.dto;

public record LoginResponse(
        String message,

        String token,

        String username,

        String email,

        String role
) {
}
