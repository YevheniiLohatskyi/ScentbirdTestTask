package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.repository.CountryRepository;
import com.yevhenii.grpc.common.DatabasePopulateServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getCountryByName(String countryName) {
        return countryRepository.getByName(countryName);
    }
}
