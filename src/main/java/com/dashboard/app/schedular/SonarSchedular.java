package com.dashboard.app.schedular;

import com.dashboard.app.service.impl.SonarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SonarSchedular {

    @Autowired
    SonarService sonarService;

    // Runs at 9 AM every day
    @Scheduled(cron = "0 0 9 * * ?")
//    @Scheduled(cron ="0 */5 * * * *")
    public void fetchProjectMetrics() {
        System.out.println("Fetch Project Metrics Cron Task started at: " + System.currentTimeMillis());
        sonarService.fetchAndSaveMetrics();
        System.out.println("Fetch Project Metrics Cron Task completed at: " + System.currentTimeMillis());

    }

    // Runs at 10 AM every day
    @Scheduled(cron = "0 0 10 * * ?")
//    @Scheduled(cron ="0 */5 * * * *")
    public void fetchProjectMetricsHistory() {
        System.out.println("Fetch Project Metrics History Cron Task started at: " + System.currentTimeMillis());
        sonarService.fetchHistoricalMetrics();
        System.out.println("Fetch Project Metrics History Cron Task completed at: " + System.currentTimeMillis());

    }
}