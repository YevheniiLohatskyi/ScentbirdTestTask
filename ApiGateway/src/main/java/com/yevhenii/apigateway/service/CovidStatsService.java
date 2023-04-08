package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.repository.CovidNewCasesRepository;
import com.yevhenii.grpc.common.CountryProto;
import com.yevhenii.grpc.common.DatabasePopulateServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CovidStatsService {

    private final DatabasePopulateServiceGrpc.DatabasePopulateServiceBlockingStub databasePopulateStub;

    private final CountryService countryService;
    private final CovidNewCasesRepository covidNewCasesRepository;

    public CovidStatsService(ManagedChannel covidApiChannel, CountryService countryService, CovidNewCasesRepository covidNewCasesRepository) {
        this.databasePopulateStub = DatabasePopulateServiceGrpc.newBlockingStub(covidApiChannel);
        this.countryService = countryService;
        this.covidNewCasesRepository = covidNewCasesRepository;
    }

    public CovidCountryStatistics getStats(Country country, LocalDate fromDate, LocalDate toDate) {
        return covidNewCasesRepository.getCountryStatsByDateBetween(country, fromDate, toDate);
    }

    public Map<String, CovidCountryStatistics> populateAndGetStatistics(List<String> countries, LocalDate fromDate, LocalDate toDate) {
        Map<String, CovidCountryStatistics> statisticsByCountry = new HashMap<>();

        for (String countryName : countries) {
            Country country = countryService.getCountryByName(countryName);
            if (country == null){
                continue;
            }
            if (country.getCovidCases() == null || country.getCovidCases().isEmpty()) {
                databasePopulateStub.populateData(CountryProto.newBuilder()
                    .setName(country.getName())
                    .setSlug(country.getSlug())
                    .setCode(country.getCode())
                    .build());
            }
            statisticsByCountry.put(countryName, getStats(country, fromDate, toDate));
        }

        return statisticsByCountry;
    }
}
