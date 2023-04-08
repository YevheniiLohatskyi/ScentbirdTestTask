package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.repository.CountryRepository;
import com.yevhenii.grpc.common.DatabasePopulateServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public Country getCountryByName(String countryName) {
        return countryRepository.getByName(countryName);
    }
}
