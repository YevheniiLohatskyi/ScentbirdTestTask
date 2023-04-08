package com.yevhenii.covidapiservice.service;

import com.yevhenii.covidapiservice.model.CountryDTO;
import com.yevhenii.covidapiservice.model.CovidDataDTO;
import com.yevhenii.covidapiservice.model.SummaryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CovidApiService {

    private static final String BASE_URL = "https://api.covid19api.com";
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
