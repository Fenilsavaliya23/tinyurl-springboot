package com.FirstProject.TinyURL.dto;

import java.util.List;

public record DashboardStatsResponse(
        long totalUrls,

        long totalClicks,

        long activeUrls,

        long expiredUrls,

        String mostClickedUrl,

        long mostClickedCount,

        List<TopUrlResponse> topUrls
) {
}
