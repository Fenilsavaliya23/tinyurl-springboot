package com.FirstProject.TinyURL.dto.admin;

public record AdminUserResponse(
        Long id,

        String username,

        String email,

        String role,

        long urlCount
) {
}
