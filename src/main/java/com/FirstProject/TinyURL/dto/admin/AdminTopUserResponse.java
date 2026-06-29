package com.FirstProject.TinyURL.dto.admin;

public record AdminTopUserResponse(
        String username,

        String email,

        long urlCount
) {
}
