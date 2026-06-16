package com.FirstProject.TinyURL.Controller;

import com.FirstProject.TinyURL.dto.ShortenUrlRequest;
import com.FirstProject.TinyURL.dto.ShortenUrlResponse;
import com.FirstProject.TinyURL.dto.UrlStatsResponse;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
//@RequestMapping("api/v1/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService urlShortenerService;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping("api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest shortenUrlRequest) {

//        The service returns the unique, generated short code (e.g., "aB1cDe").
        String shortCode = urlShortenerService.shortenUrl(
                shortenUrlRequest.URL(),
                shortenUrlRequest.customAlias(),
                shortenUrlRequest.hoursToExpire()
                );

//       Construct the full, user-facing short URL.
//        String fullShortUrl = "http://localhost:8080/" + shortCode;
        String fullShortUrl = baseUrl + "/r/" + shortCode;

//        create a response DTO
        ShortenUrlResponse shortenUrlResponse = new ShortenUrlResponse(fullShortUrl);

        return ResponseEntity.ok(shortenUrlResponse);
    }

    @GetMapping("/r/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) throws UrlNotFoundException {

        String originalURL = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);

//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setLocation(URI.create(originalURL));

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalURL)).build();
    }

    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) throws UrlNotFoundException {

        UrlStatsResponse stats =
                urlShortenerService.getStats(shortCode);

        return ResponseEntity.ok(stats);

    }

}
