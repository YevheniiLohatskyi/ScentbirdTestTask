package com.yevhenii.covidapiservice.service;

import com.yevhenii.covidapiservice.model.CountryDTO;
import com.yevhenii.covidapiservice.model.CovidDataDTO;
import com.yevhenii.covidapiservice.model.SummaryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CovidApiService {

    @Value("${covidapi.base-url}")
    public static String BASE_URL;

    private final RestTemplate restTemplate;
    private final CircuitBreaker circuitBreaker;

    public CovidApiService(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreaker = circuitBreakerFactory.create("covid-api");
    }

    public List<CountryDTO> getAllCountries() {
        return Arrays.asList(circuitBreaker.run(
            () -> restTemplate.getForObject(BASE_URL + "/countries", CountryDTO[].class),
            throwable -> new CountryDTO[0]));
    }

    public List<CovidDataDTO> getDataForCountry(String countrySlug) {
        return Arrays.asList(circuitBreaker.run(
            () -> restTemplate.getForObject(
                BASE_URL + String.format("/total/dayone/country/%s/status/confirmed", countrySlug),
                CovidDataDTO[].class),
            throwable -> new CovidDataDTO[0]));
    }

    public SummaryDTO getSummary() {
        return circuitBreaker.run(
            () -> restTemplate.getForObject(BASE_URL + "/summary", SummaryDTO.class),
            throwable -> new SummaryDTO());
    }
}
