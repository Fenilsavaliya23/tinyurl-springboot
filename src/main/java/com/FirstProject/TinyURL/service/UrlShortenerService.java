package com.FirstProject.TinyURL.service;

import com.FirstProject.TinyURL.Model.UrlMapping;
import com.FirstProject.TinyURL.dto.UrlStatsResponse;
import com.FirstProject.TinyURL.exception.AliasAlreadyExistsException;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Transactional
    public String shortenUrl(String originalUrl, String customAlias, Integer hoursToExpire) {

        // If user provide customAlias
        if(StringUtils.hasText(customAlias)){

            // Check if the alias is already in use.
            if(urlMappingRepository.findByShortCode(customAlias).isPresent()){
                    throw new AliasAlreadyExistsException("Alias '" + customAlias + "' already exists");
            }

            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreatedDate(LocalDateTime.now());
            urlMapping.setShortCode(customAlias); // use short code as a custom shortCode

            if(hoursToExpire != null && hoursToExpire > 0){
                urlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            urlMappingRepository.save(urlMapping);

            return customAlias;
        }

        // If user don't provide customAlias, then we generate by encodeBase62
        else {
            // 1. create a Fresh Entity
            UrlMapping urlMapping = new  UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreatedDate(LocalDateTime.now());

            // This guarantees uniqueness.
            urlMapping.setShortCode(UUID.randomUUID().toString());

            if(hoursToExpire != null && hoursToExpire > 0){
                urlMapping.setExpirationDate(LocalDateTime.now().plusHours(hoursToExpire));
            }

            // 2. First save to generate the unique ID
            UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

            // 3. Generate(62 base) the short code from the ID
            String shortCode = encodeBase62(savedEntity.getId());

            // 4. update the entity with the generated the shortcode
            savedEntity.setShortCode(shortCode);

            // 5. Second save to persist the short code in the database
            // Because savedEntity now has a non-null ID, JPA knows to perform an
            // UPDATE operation instead of an INSERT. This call persists the shortCode
            // to the database, completing the record.
            // if id is null so, our JPA perform an insert operation.
            urlMappingRepository.save(savedEntity);

            return shortCode;

        }
    }

    public String getOriginalUrlAndIncrementClicks(String shortCode) throws UrlNotFoundException {

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not Found for the short Code: " + shortCode));

        if(urlMapping.getExpirationDate() != null && urlMapping.getExpirationDate().isBefore(LocalDateTime.now())){
            throw  new UrlNotFoundException("This link has expired and is no longer active.");
        }

        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMapping.setLastAccessedAt(LocalDateTime.now());
        urlMappingRepository.save(urlMapping);
        return urlMapping.getOriginalUrl();
    }

    public UrlStatsResponse getStats(String shortCode) throws UrlNotFoundException {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(()-> new UrlNotFoundException("No statistics Found for the short Code: " + shortCode));

//        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();
        String fullShortUrl = baseUrl + "/r/" + urlMapping.getShortCode();

        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreatedDate(),
                urlMapping.getExpirationDate(),
                urlMapping.getLastAccessedAt(),
                urlMapping.getClickCount()
        );

    }

    private String encodeBase62(Long number) {
        if (number == 0) {
            return String.valueOf(UrlShortenerService.BASE62_CHARS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        long l = number;

        while (l > 0) {
            int reminder = (int) (l % 62);
            sb.append(UrlShortenerService.BASE62_CHARS.charAt(reminder));
            l /= 62;
        }

        return sb.reverse().toString();
    }
}
