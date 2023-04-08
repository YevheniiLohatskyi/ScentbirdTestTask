package com.yevhenii.apigateway.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GrpcChannelConfig {
    @Bean
    public ManagedChannel covidApiChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext()
            .build();
    }
}
