package com.zwash.booking.grpc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.grpc.UserServiceGrpc.UserServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class UserServiceGrpcClient {

	@Bean(name = "userServiceChannel")
    public ManagedChannel userServiceChannel() {
        return ManagedChannelBuilder
        		.forAddress("127.0.0.1", 9091)
                .usePlaintext()
                .build();
    }

	@Bean
	UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub(
	        @Qualifier("userServiceChannel") ManagedChannel channel) {
	    return UserServiceGrpc.newBlockingStub(channel);
	}

}
