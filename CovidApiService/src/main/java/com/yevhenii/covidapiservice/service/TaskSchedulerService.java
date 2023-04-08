package com.yevhenii.covidapiservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TaskSchedulerService {

    private final DatabasePopulateService databasePopulateService;

    public TaskSchedulerService(DatabasePopulateService databasePopulateService) {
        this.databasePopulateService = databasePopulateService;
    }

    // runs every day at 6:00 am
    @Scheduled(cron = "${daily-update-cron}")
    void populateDataForToday() {
        databasePopulateService.populateDataForToday();
    }
}
