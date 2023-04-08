package com.yevhenii.apigateway.controller;

import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.service.CovidStatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class CovidController {

    private final CovidStatsService covidStatsService;

    public CovidController(CovidStatsService covidStatsService) {
        this.covidStatsService = covidStatsService;
    }

    @GetMapping("/new-cases-stats")
    public ResponseEntity<Map<String, CovidCountryStatistics>> getNewCasesStats(
        @RequestParam("countries") List<String> countries,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Map<String, CovidCountryStatistics> statisticsByCountry =
            covidStatsService.getStatisticsByCountry(countries, fromDate, toDate);

        return new ResponseEntity<>(statisticsByCountry, HttpStatus.OK);
    }
}
