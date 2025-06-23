package com.zwash.booking.grpc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zwash.common.grpc.StationServiceGrpc;
import com.zwash.common.grpc.StationServiceGrpc.StationServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class StationServiceGrpcClient {

	@Bean(name = "stationServiceChannel")
    public ManagedChannel stationServiceChannel() {
        return ManagedChannelBuilder
                .forAddress("localhost", 8081) 
                .usePlaintext()
                .build();
    }

	@Bean
	StationServiceGrpc.StationServiceBlockingStub stationServiceBlockingStub(
	        @Qualifier("stationServiceChannel") ManagedChannel channel) {
	    return StationServiceGrpc.newBlockingStub(channel);
	}

}
