package com.yevhenii.covidapiservice.service;

import com.yevhenii.covidapiservice.model.CountryDTO;
import com.yevhenii.covidapiservice.model.CovidDataDTO;
import com.yevhenii.covidapiservice.model.SummaryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CovidApiService {

    @Value("${covidapi.base-url")
    public static String BASE_URL;
    public static final String COUNTRY_DATA = "/total/dayone/country/%s/status/confirmed";
    public static final String COUNTRIES = "/countries";
    public static final String SUMMARY = "/summary";

    private final RestTemplate restTemplate;

    public CovidApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CountryDTO[] getAllCountries() {
        String url = BASE_URL + COUNTRIES;
        return restTemplate.getForObject(url, CountryDTO[].class);
    }

    public CovidDataDTO[] getDataForCountry(String countrySlug) {
        String url = BASE_URL + String.format(COUNTRY_DATA, countrySlug);
        return restTemplate.getForObject(url, CovidDataDTO[].class);
    }

    public SummaryDTO getSummary() {
        String url = BASE_URL + SUMMARY;
        return restTemplate.getForObject(url, SummaryDTO.class);
    }
}
