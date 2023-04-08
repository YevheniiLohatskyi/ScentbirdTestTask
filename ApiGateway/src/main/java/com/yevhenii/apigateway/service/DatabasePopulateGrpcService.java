package com.yevhenii.apigateway.service;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.grpc.common.CountryProto;
import com.yevhenii.grpc.common.DatabasePopulateServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabasePopulateGrpcService {

    private final DatabasePopulateServiceGrpc.DatabasePopulateServiceBlockingStub databasePopulateStub;

    public DatabasePopulateGrpcService(ManagedChannel covidApiChannel) {
        this.databasePopulateStub = DatabasePopulateServiceGrpc.newBlockingStub(covidApiChannel);
    }

    public void populateDataForCountry(Country country) {
        databasePopulateStub.populateData(CountryProto.newBuilder()
            .setName(country.getName())
            .setSlug(country.getSlug())
            .setCode(country.getCode())
            .build());
    }
}
