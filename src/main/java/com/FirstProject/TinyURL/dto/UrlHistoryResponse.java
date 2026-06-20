package com.FirstProject.TinyURL.dto;

import java.time.LocalDateTime;

public record UrlHistoryResponse(
        String originalUrl,

        String shortCode,

        String shortUrl,

        long clickCount,

        LocalDateTime createdDate,

        LocalDateTime expirationDate
) {
}
