package com.FirstProject.TinyURL.dto;

public record DashboardStatsResponse(
        long totalUrls,

        long totalClicks,

        long activeUrls,

        long expiredUrls,

        String mostClickedUrl,

        long mostClickedCount
) {
}
