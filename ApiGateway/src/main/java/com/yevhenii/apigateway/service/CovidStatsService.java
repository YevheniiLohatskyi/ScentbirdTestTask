package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.repository.CovidNewCasesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CovidStatsService {

    private final CountryService countryService;
    private final CovidNewCasesRepository covidNewCasesRepository;
    private final DatabasePopulateGrpcService databasePopulateGrpcService;

    public CovidStatsService(CountryService countryService, CovidNewCasesRepository covidNewCasesRepository, DatabasePopulateGrpcService databasePopulateGrpcService) {
        this.countryService = countryService;
        this.covidNewCasesRepository = covidNewCasesRepository;
        this.databasePopulateGrpcService = databasePopulateGrpcService;
    }

    public CovidCountryStatistics getStatistics(Country country, LocalDate fromDate, LocalDate toDate) {
        return covidNewCasesRepository.getCountryStatsByDateBetween(country, fromDate, toDate);
    }

    public Map<String, CovidCountryStatistics> getStatisticsByCountry(List<String> countries, LocalDate fromDate, LocalDate toDate) {
        Map<String, CovidCountryStatistics> statisticsByCountry = new HashMap<>();

        for (String countryName : countries) {
            Country country = countryService.getCountryByName(countryName);
            if (country == null){
                continue;
            }
            if (country.getCovidCases() == null || country.getCovidCases().isEmpty()) {
                databasePopulateGrpcService.populateDataForCountry(country);
            }
            statisticsByCountry.put(countryName, getStatistics(country, fromDate, toDate));
        }

        return statisticsByCountry;
    }
}
