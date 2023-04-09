package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.repository.CovidNewCasesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CovidCountryStatistics getStatistics(Country country, LocalDate fromDate, LocalDate toDate) {
        return covidNewCasesRepository.getCountryStatsByDateBetween(country, fromDate, toDate);
    }

    @Transactional
    public Map<String, CovidCountryStatistics> getStatisticsByCountry(List<String> countries, LocalDate fromDate, LocalDate toDate) {
        countries.stream()
            .map(countryService::getCountryByName)
            .filter(Objects::nonNull)
            .filter(country -> nonNull(country.getCovidCases()))
            .filter(country -> !country.getCovidCases().isEmpty())
            .forEach(databasePopulateGrpcService::populateDataForCountry);

        return countries.stream()
            .map(countryService::getCountryByName)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Country::getName, country -> getStatistics(country, fromDate, toDate)));
    }
}
