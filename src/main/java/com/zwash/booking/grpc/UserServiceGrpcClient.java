package com.zwash.booking.grpc;

import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.grpc.UserServiceGrpc.UserServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class UserServiceGrpcClient {

    private UserServiceBlockingStub userStub;
    private ManagedChannel channel;

    @PostConstruct
    public void init() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 8085) 
                .usePlaintext() // for local, remove in production
                .build();

        this.userStub = UserServiceGrpc.newBlockingStub(channel);
    }

    public UserServiceBlockingStub getStub() {
        return userStub;
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }
}
