package com.FirstProject.TinyURL.dto;

public record TopUrlResponse(
        String shortUrl,

        long clickCount
) {
}
