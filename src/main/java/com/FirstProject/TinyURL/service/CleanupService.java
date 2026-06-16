package com.FirstProject.TinyURL.service;

import com.FirstProject.TinyURL.repository.UrlMappingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CleanupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupService.class);

    private final UrlMappingRepository urlMappingRepository;

    /*
     * A scheduled job that runs automatically to clean up expired URL mappings.
     *
     * @Scheduled: This annotation marks the method as a scheduled task.
     *   - cron = "0 0 1 * * ?": This cron expression configures the task to run
     *     every day at 1:00 AM.
     *     - Second: 0
     *     - Minute: 0
     *     - Hour: 1
     *     - Day of Month: * (any)
     *     - Month: * (any)
     *     - Day of Week: ? (not specified)
     *
     * This method will be executed by Spring's task scheduler in a background thread.
     */
    // Execute at 1:00:00 AM, on every day, of every month. This is our daily cleanup schedule.
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void cleanupExpiredUrls(){
        LOGGER.info("Starting scheduled job: Cleaning up expired URL mappings...");

        LocalDateTime now = LocalDateTime.now();

        long deleteCount = urlMappingRepository.deleteByExpirationDateBefore(now);

        if(deleteCount > 0){
            LOGGER.info("Finished scheduled job: Successfully deleted {} expired URL mappings.", deleteCount);
        }
        else{
            LOGGER.info("Finished scheduled job: No expire URL mappings found to delete.");
        }
    }

}
