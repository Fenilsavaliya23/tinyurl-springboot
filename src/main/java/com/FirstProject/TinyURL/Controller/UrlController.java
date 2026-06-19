package com.FirstProject.TinyURL.Controller;

import com.FirstProject.TinyURL.dto.*;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.service.QrCodeService;
import com.FirstProject.TinyURL.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
//@RequestMapping("api/v1/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService urlShortenerService;
    private final QrCodeService qrCodeService;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping("api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest shortenUrlRequest) {

//        The service returns the unique, generated short code (e.g., "aB1cDe").
        String shortCode = urlShortenerService.shortenUrl(
                shortenUrlRequest.url(),
                shortenUrlRequest.customAlias(),
                shortenUrlRequest.hoursToExpire(),
                shortenUrlRequest.userEmail()
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

        if (!originalURL.startsWith("http://")
                && !originalURL.startsWith("https://")) {

            originalURL = "https://" + originalURL;
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalURL)).build();
    }

    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) throws UrlNotFoundException {

        UrlStatsResponse stats =
                urlShortenerService.getStats(shortCode);

        return ResponseEntity.ok(stats);

    }

    @GetMapping("/api/v1/url/my-urls")
    public ResponseEntity<List<UrlHistoryResponse>> getMyUrls(Authentication authentication) {

        return ResponseEntity.ok(urlShortenerService.getMyUrls(authentication.getName()));
    }

    @GetMapping("/api/v1/url/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(Authentication authentication) throws Exception {
        return ResponseEntity.ok(urlShortenerService.getDashboardStats(authentication.getName()));
    }

    @DeleteMapping("/api/v1/url/{shortCode}")
    public ResponseEntity<String> deleteUrl(@PathVariable String shortCode, Authentication authentication) throws Exception {

        urlShortenerService.deleteUrl(shortCode, authentication.getName());

        return ResponseEntity.ok("URL deleted Successfully");
    }

    @PatchMapping("/api/v1/url/{shortCode}/alias")
    public ResponseEntity<String> updateAlias(
            @PathVariable String shortCode, @Valid @RequestBody UpdateAliasRequest req, Authentication authentication) throws Exception {

        urlShortenerService.updateAlias(authentication.getName(), shortCode, req.newAlias());

        return ResponseEntity.ok("Alias updated Successfully");
    }

    @GetMapping(value= "/api/v1/url/{shortCode}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQrCode(@PathVariable String shortCode) throws Exception {

        String shortUrl = baseUrl + "/r/" + shortCode;

        byte[] qrImage = qrCodeService.generateQrCode(shortUrl);
        return ResponseEntity.ok(qrImage);

    }

}
