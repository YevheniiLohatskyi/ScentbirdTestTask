package com.yevhenii.covidapiservice.config;

import com.yevhenii.covidapiservice.service.DatabasePopulateService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port}")
    private int grpcServerPort;

    private final DatabasePopulateService databasePopulateService;

    public GrpcServerConfig(DatabasePopulateService databasePopulateService) {
        this.databasePopulateService = databasePopulateService;
    }

    @Bean
    public Server grpcServer() throws IOException {
        Server server = ServerBuilder.forPort(grpcServerPort)
            .addService(databasePopulateService)
            .build();
        server.start();
        return server;
    }
}
