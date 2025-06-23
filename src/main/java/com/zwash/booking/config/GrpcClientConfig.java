package com.zwash.booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;
import com.zwash.common.grpc.CarWashingProgramServiceGrpc;
import com.zwash.common.grpc.UserServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.car-washing-program.host}")
    private String host="localhost";

    @Value("${grpc.car-washing-program.port}")
    private int port=9099;

    @Bean
    public CarWashingProgramServiceGrpc.CarWashingProgramServiceBlockingStub carWashingProgramStub() {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext() 
            .build();

        return CarWashingProgramServiceGrpc.newBlockingStub(channel);
    }
    
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userStub() {
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 8085) 
            .usePlaintext()
            .build();
        return UserServiceGrpc.newBlockingStub(channel);
    }

}
