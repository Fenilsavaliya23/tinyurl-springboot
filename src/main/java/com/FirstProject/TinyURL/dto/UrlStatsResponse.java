package com.FirstProject.TinyURL.dto;


import java.time.LocalDateTime;

public record UrlStatsResponse(

    String originalUrl,
    String shortUrl,
    LocalDateTime creationDate,
    LocalDateTime expirationDate,
    LocalDateTime lastAccessedAt,
    long clickCount
){
}
