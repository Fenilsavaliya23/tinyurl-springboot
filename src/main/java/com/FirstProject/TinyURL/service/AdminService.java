package com.FirstProject.TinyURL.service;

import com.FirstProject.TinyURL.Model.Role;
import com.FirstProject.TinyURL.Model.UrlMapping;
import com.FirstProject.TinyURL.Model.User;
import com.FirstProject.TinyURL.dto.admin.AdminDashboardResponse;
import com.FirstProject.TinyURL.dto.admin.AdminTopUserResponse;
import com.FirstProject.TinyURL.dto.admin.AdminUrlResponse;
import com.FirstProject.TinyURL.dto.admin.AdminUserResponse;
import com.FirstProject.TinyURL.exception.UrlNotFoundException;
import com.FirstProject.TinyURL.exception.UserNotFoundException;
import com.FirstProject.TinyURL.repository.UrlMappingRepository;
import com.FirstProject.TinyURL.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UrlMappingRepository urlMappingRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public AdminDashboardResponse getAdminDashboard() {

        long totalUsers =  userRepository.count();
        long totalUrls = urlMappingRepository.count();

        long totalClicks = urlMappingRepository.findAll()
                .stream()
                .mapToLong(UrlMapping::getClickCount)
                .sum();

        long activeUrls = urlMappingRepository.findAll()
                .stream()
                .filter(url -> url.getExpirationDate() == null
                                        || url.getExpirationDate().isAfter(LocalDateTime.now())
                )
                .count();

        long expiredUrls = urlMappingRepository.findAll()
                .stream()
                .filter(url -> url.getExpirationDate() != null
                                        && url.getExpirationDate().isBefore(LocalDateTime.now()))
                .count();

        return new AdminDashboardResponse(totalUsers, totalUrls, totalClicks, activeUrls, expiredUrls);
    }

    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name(),
                        urlMappingRepository.countByOwner(user)
                ))
                .toList();
    }

    public List<AdminUrlResponse> getAllUrls() {

        return urlMappingRepository.findAllByOrderByCreatedDateDesc()
                .stream()
                .map(url -> new AdminUrlResponse(
                        url.getId(),
                        url.getOwner() == null
                                ? "SYSTEM"
                                : url.getOwner().getUsername(),
                        url.getOwner() == null
                                ? "SYSTEM_EMAIL"
                                : url.getOwner().getEmail(),
                        baseUrl + "/r/" + url.getShortCode(),
                        url.getOriginalUrl(),
                        url.getClickCount(),
                        url.getCreatedDate(),
                        url.getExpirationDate()
                ))
                .toList();
    }

    public List<AdminUrlResponse> getTopUrls() {

        return urlMappingRepository
                .findTop10ByOrderByClickCountDesc()
                .stream()
                .map(url -> new AdminUrlResponse(
                        url.getId(),
                        url.getOwner() == null
                                ? "SYSTEM"
                                : url.getOwner().getUsername(),

                        url.getOwner() == null
                                ? "SYSTEM_EMAIL"
                                : url.getOwner().getEmail(),

                        baseUrl + "/r/" + url.getShortCode(),

                        url.getOriginalUrl(),

                        url.getClickCount(),

                        url.getCreatedDate(),

                        url.getExpirationDate()

                ))
                .toList();
    }

    public List<AdminTopUserResponse> getTopUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminTopUserResponse(

                        user.getUsername(),

                        user.getEmail(),

                        urlMappingRepository.countByOwner(user)

                ))
                .sorted((a, b) ->
                        Long.compare(b.urlCount(),a.urlCount())
                )
                .limit(10)
                .toList();
    }

    public void promoteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }

    public void demoteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + userId));

        List<UrlMapping> userUrls = urlMappingRepository.findByOwner(user);

        for(UrlMapping url: userUrls) {
            url.setOwner(null);
        }

        urlMappingRepository.saveAll(userUrls);

        userRepository.delete(user);
    }

    public void deleteUrl(Long urlId) throws UrlNotFoundException {

        UrlMapping url = urlMappingRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException("Url not Found with id: " + urlId));

        urlMappingRepository.delete(url);

    }

}
