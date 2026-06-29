package com.FirstProject.TinyURL.dto.admin;

import java.time.LocalDateTime;

public record AdminUrlResponse(

        Long urlId,

        String ownerName,

        String ownerEmail,

        String shortUrl,

        String originalUrl,

        long clickCount,

        LocalDateTime createdDate,

        LocalDateTime expirationDate
) {
}
