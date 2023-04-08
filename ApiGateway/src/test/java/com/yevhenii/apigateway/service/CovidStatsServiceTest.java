package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.model.CovidNewCases;
import com.yevhenii.apigateway.repository.CovidNewCasesRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CovidStatsServiceTest {

    private final CountryService countryServiceMock = mock(CountryService.class);
    private final CovidNewCasesRepository covidNewCasesRepositoryMock = mock(CovidNewCasesRepository.class);
    private final DatabasePopulateGrpcService databasePopulateGrpcServiceMock = mock(DatabasePopulateGrpcService.class);

    private final CovidStatsService covidStatsService = new CovidStatsService(countryServiceMock, covidNewCasesRepositoryMock, databasePopulateGrpcServiceMock);

    @Test
    public void testGetStats() {
        Country country = new Country("Germany", "germany", "de");
        LocalDate fromDate = LocalDate.now().minusMonths(1);
        LocalDate toDate = LocalDate.now();
        CovidCountryStatistics expectedStats = new CovidCountryStatistics(10, 3);
        when(covidNewCasesRepositoryMock.getCountryStatsByDateBetween(
            country, fromDate, toDate)
        ).thenReturn(expectedStats);

        CovidCountryStatistics result = covidStatsService.getStatistics(country, fromDate, toDate);

        assertEquals(expectedStats, result);
    }

    @Test
    public void testPopulateAndGetStatistics_countryNotFound() {
        List<String> countryNames = List.of("USA", "Canada");
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        when(countryServiceMock.getCountryByName("USA")).thenReturn(null);
        Map<String, CovidCountryStatistics> result = covidStatsService.getStatisticsByCountry(countryNames, fromDate, toDate);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPopulateAndGetStatistics_populateData() {
        List<String> countryNames = List.of("USA", "Canada");
        LocalDate fromDate = LocalDate.now().minusMonths(1);
        LocalDate toDate = LocalDate.now();
        Country usa = new Country("USA", "usa", "us");
        usa.setCovidCases(Collections.emptyList());
        Country canada = new Country("Canada", "canada", "ca");
        canada.setCovidCases(List.of(
            new CovidNewCases(canada, fromDate, 14),
            new CovidNewCases(canada, toDate, 7)));

        CovidCountryStatistics expectedStatsUsa = new CovidCountryStatistics(10, 0);
        CovidCountryStatistics expectedStatsCanada = new CovidCountryStatistics(14, 7);

        when(countryServiceMock.getCountryByName("USA")).thenReturn(usa);
        when(countryServiceMock.getCountryByName("Canada")).thenReturn(canada);
        doNothing().when(databasePopulateGrpcServiceMock).populateDataForCountry(any(Country.class));
        when(covidNewCasesRepositoryMock.getCountryStatsByDateBetween(usa, fromDate, toDate)).thenReturn(expectedStatsUsa);
        when(covidNewCasesRepositoryMock.getCountryStatsByDateBetween(canada, fromDate, toDate)).thenReturn(expectedStatsCanada);

        Map<String, CovidCountryStatistics> result = covidStatsService.getStatisticsByCountry(countryNames, fromDate, toDate);

        assertEquals(2, result.size());
        assertEquals(expectedStatsUsa, result.get("USA"));
        assertEquals(expectedStatsCanada, result.get("Canada"));
    }
}
