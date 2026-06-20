package com.FirstProject.TinyURL.service;

import com.FirstProject.TinyURL.Model.UrlMapping;
import com.FirstProject.TinyURL.Model.User;
import com.FirstProject.TinyURL.dto.DashboardStatsResponse;
import com.FirstProject.TinyURL.dto.UrlHistoryResponse;
import com.FirstProject.TinyURL.dto.UrlStatsResponse;
import com.FirstProject.TinyURL.exception.AliasAlreadyExistsException;
import com.FirstProject.TinyURL.exception.UnauthorizedUrlAccessException;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.exception.UserNotFoundException;
import com.FirstProject.TinyURL.repository.UrlMappingRepository;
import com.FirstProject.TinyURL.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Transactional
    public String shortenUrl(String originalUrl, String customAlias, Integer hoursToExpire, String userEmail) {

        User owner = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // If user provide customAlias
        if(StringUtils.hasText(customAlias)){

            // Check if the alias is already in use.
            if(urlMappingRepository.findByShortCode(customAlias).isPresent()){
                    throw new AliasAlreadyExistsException("Alias '" + customAlias + "' already exists");
            }

            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreatedDate(LocalDateTime.now());
            urlMapping.setOwner(owner);
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
            urlMapping.setOwner(owner);
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

    public List<UrlHistoryResponse> getMyUrls(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return urlMappingRepository
                .findByOwnerOrderByCreatedDateDesc(user)
                .stream()
                .map(url -> new UrlHistoryResponse(
                        url.getOriginalUrl(),
                        url.getShortCode(),
                        baseUrl+ "/r/" + url.getShortCode(),
                        url.getClickCount(),
                        url.getCreatedDate(),
                        url.getExpirationDate()
                ))
                .toList();

    }

    @Transactional
    public void deleteUrl(String shortCode, String UserEmail) throws Exception {
        User user = userRepository.findByEmail(UserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not Found"));
        
        if(!urlMapping.getOwner().getEmail().equals(UserEmail)){
            throw new UnauthorizedUrlAccessException("You are not allowed to delete another user's URL");
        }

        urlMappingRepository.delete(urlMapping);
    }

    @Transactional
    public void updateAlias(String UserEmail, String shortCode, String newAlias) throws Exception {
        User user = userRepository.findByEmail(UserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not Found"));

        if(!urlMapping.getOwner().getEmail().equals(UserEmail)){
            throw new UnauthorizedUrlAccessException("You are not allowed to edit another user's URL");
        }

        if(urlMappingRepository.existsByShortCode(newAlias)){
            throw new AliasAlreadyExistsException("Alias: '" + newAlias + "' already exists");
        }

        urlMapping.setShortCode(newAlias);

        urlMappingRepository.save(urlMapping);
    }

    public DashboardStatsResponse getDashboardStats(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<UrlMapping> urls = urlMappingRepository.findByOwnerOrderByCreatedDateDesc(user);

        UrlMapping mostClicked = urls.stream().max(java.util.Comparator.comparingLong
                        (UrlMapping::getClickCount))
                        .orElse(null);

        String mostClickedUrl = mostClicked == null ? "N/A" : mostClicked.getOriginalUrl();

        long mostClickedCount = mostClicked == null ? 0 : mostClicked.getClickCount();

        return new DashboardStatsResponse(
                urls.size(),

                urls.stream().mapToLong(UrlMapping::getClickCount).sum(),

                urls.stream().filter(url ->
                        url.getExpirationDate() == null
                        || url.getExpirationDate().isAfter(LocalDateTime.now())
                ).count(),

                urls.stream().filter(url ->
                        url.getExpirationDate() != null
                        && url.getExpirationDate().isBefore(LocalDateTime.now())
                ).count(),

                mostClickedUrl,

                mostClickedCount
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
