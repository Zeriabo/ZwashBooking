package com.zwash.booking.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;
import com.zwash.common.grpc.CarWashingProgramServiceGrpc;
import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.car.grpc.CarServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
@Configuration
public class GrpcClientConfig {

    private final GrpcProperties grpcProperties;

    public GrpcClientConfig(GrpcProperties grpcProperties) {
        this.grpcProperties = grpcProperties;
    }

    @Bean
    @Qualifier("userChannel")
    public ManagedChannel userChannel() {
        GrpcProperties.Service service = grpcProperties.getUserService();
        return ManagedChannelBuilder
                .forAddress(service.getHost(), service.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    @Qualifier("stationChannel")
    public ManagedChannel stationChannel() {
        GrpcProperties.Service service = grpcProperties.getStationService();
        return ManagedChannelBuilder
                .forAddress(service.getHost(), service.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    @Qualifier("bookingChannel")
    public ManagedChannel bookingChannel() {
        GrpcProperties.Service service = grpcProperties.getBookingService();
        return ManagedChannelBuilder
                .forAddress(service.getHost(), service.getPort())
                .usePlaintext()
                .build();
    }

    @Bean
    @Qualifier("carServiceChannel")
    public ManagedChannel carServiceChannel() {
        GrpcProperties.Service service = grpcProperties.getCarService();
        if (service == null) {
            throw new IllegalStateException("grpc.car-service is not defined in application.yml");
        }
        return ManagedChannelBuilder
                .forAddress(service.getHost(), service.getPort())
                .usePlaintext()
                .build();
    }

    // Stubs
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(
            @Qualifier("userChannel") ManagedChannel userChannel) {
        return UserServiceGrpc.newBlockingStub(userChannel);
    }

    @Bean
    public CarWashingProgramServiceGrpc.CarWashingProgramServiceBlockingStub carWashingProgramStub(
            @Qualifier("stationChannel") ManagedChannel stationChannel) {
        return CarWashingProgramServiceGrpc.newBlockingStub(stationChannel);
    }

    @Bean
    public CarServiceGrpc.CarServiceBlockingStub carServiceBlockingStub(
            @Qualifier("carServiceChannel") ManagedChannel carServiceChannel) {
        return CarServiceGrpc.newBlockingStub(carServiceChannel);
    }
}
