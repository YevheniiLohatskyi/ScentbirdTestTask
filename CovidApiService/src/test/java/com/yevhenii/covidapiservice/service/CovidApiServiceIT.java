package com.yevhenii.covidapiservice.service;

import com.yevhenii.covidapiservice.model.CountryDTO;
import com.yevhenii.covidapiservice.model.CovidDataDTO;
import com.yevhenii.covidapiservice.model.SummaryDTO;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CovidApiServiceIT {

    private final CovidApiService covidApiService = new CovidApiService(new RestTemplate());

    @Test
    void testGetAllCountries() {
        CountryDTO[] countries = covidApiService.getAllCountries();
        assertNotNull(countries);
        assertTrue(countries.length > 0);
        assertTrue(StringUtils.isNotBlank(countries[0].getCountry()));
        assertTrue(StringUtils.isNotBlank(countries[0].getSlug()));
        assertTrue(StringUtils.isNotBlank(countries[0].getIso2()));
    }

    @Test
    void testGetDataForCorrectCountrySlug() {
        CovidDataDTO[] covidData = covidApiService.getDataForCountry("germany");
        assertNotNull(covidData);
        assertTrue(covidData.length > 0);
        assertTrue(StringUtils.isNotBlank(covidData[0].getCountry()));
        assertNotNull(covidData[0].getDate());
    }

    @Test
    void testGetDataShouldThrowExceptionForIncorrectCountrySlug() {
        assertThrows(HttpClientErrorException.class, () -> covidApiService.getDataForCountry("germanya"));
    }

    @Test
    void testGetSummary() {
        SummaryDTO summary = covidApiService.getSummary();
        assertNotNull(summary);
        assertEquals(LocalDate.now(), summary.getDate());
        assertTrue(summary.getCountries().size() > 0);
    }
}
