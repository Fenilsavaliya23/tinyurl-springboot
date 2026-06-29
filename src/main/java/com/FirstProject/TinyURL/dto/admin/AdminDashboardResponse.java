package com.FirstProject.TinyURL.dto.admin;

public record AdminDashboardResponse(
        long totalUsers,

        long totalUrls,

        long totalClicks,

        long activeUrls,

        long expiredUrls
) {
}
