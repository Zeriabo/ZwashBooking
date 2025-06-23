package com.zwash.booking.config;

import com.zwash.common.grpc.CarServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CarServiceGrpcClientConfig {

    @Bean
    public ManagedChannel carServiceChannel() {
        return ManagedChannelBuilder
            .forAddress("localhost", 8089) 
            .usePlaintext()               
            .build();
    }

    @Bean
    public CarServiceGrpc.CarServiceBlockingStub carServiceStub(
        @Qualifier("carServiceChannel") ManagedChannel channel) {
        return CarServiceGrpc.newBlockingStub(channel);
    }
}
		