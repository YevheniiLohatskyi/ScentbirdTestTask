package com.yevhenii.covidapiservice.service;

import com.yevhenii.covidapiservice.model.Country;
import com.yevhenii.covidapiservice.model.CovidDataDTO;
import com.yevhenii.covidapiservice.model.CovidNewCases;
import com.yevhenii.covidapiservice.model.SummaryDTO;
import com.yevhenii.covidapiservice.repository.CountryRepository;
import com.yevhenii.covidapiservice.repository.CovidNewCasesRepository;
import com.yevhenii.grpc.common.Bool;
import com.yevhenii.grpc.common.DatabasePopulateServiceGrpc;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class DatabasePopulateService extends DatabasePopulateServiceGrpc.DatabasePopulateServiceImplBase {

    private final CovidNewCasesRepository covidNewCasesRepository;
    private final CountryRepository countryRepository;
    private final CovidApiService covidApiService;

    public DatabasePopulateService(CovidNewCasesRepository covidNewCasesRepository,
                                   CountryRepository countryRepository,
                                   CovidApiService covidApiService) {
        this.covidNewCasesRepository = covidNewCasesRepository;
        this.countryRepository = countryRepository;
        this.covidApiService = covidApiService;
    }

    @PostConstruct
    @Transactional
    public void populateCountries() {
        if (countryRepository.findAll().isEmpty()) {
            countryRepository.saveAll(
                covidApiService.getAllCountries().stream()
                    .map(covidCountry ->
                        new Country(covidCountry.getCountry(), covidCountry.getSlug(), covidCountry.getIso2()))
                    .toList()
            );
        }
    }

    @Override
    @Transactional
    public void populateData(com.yevhenii.grpc.common.Country request, StreamObserver<Bool> responseObserver) {
        Country country = countryRepository.getByCode(request.getCode());
        Map<LocalDate, Integer> allData =
            covidApiService.getDataForCountry(country.getSlug()).stream()
                .collect(Collectors.toMap(c -> c.getDate().toLocalDate(), CovidDataDTO::getCases));
        covidNewCasesRepository.saveAll(calculateDailyNewCases(allData, country));

        responseObserver.onNext(Bool.newBuilder().build());
        responseObserver.onCompleted();
    }

    private List<CovidNewCases> calculateDailyNewCases(Map<LocalDate, Integer> totalCasesByDate, Country country) {
        return totalCasesByDate.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> totalCasesByDate.containsKey(entry.getKey().minusDays(1))
                ? new CovidNewCases(country, entry.getKey(),
                    entry.getValue() - totalCasesByDate.get(entry.getKey().minusDays(1)))
                : new CovidNewCases(country, entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    void populateDataForToday() {
        Map<String, Country> countriesByCode = countryRepository.findAll().stream()
            .collect(Collectors.toMap(Country::getCode, Function.identity()));
        SummaryDTO summary = Objects.requireNonNull(covidApiService.getSummary());

        covidNewCasesRepository.saveAll(summary.getCountries().stream()
            .map(c -> new CovidNewCases(countriesByCode.get(c.getIso2()), summary.getDate(), c.getNewConfirmed()))
            .toList());
    }
}
