package com.zwash.booking.grpc;


import com.zwash.common.grpc.UserServiceGrpc;
import com.zwash.common.grpc.UserServiceProto.TokenRequest;
import com.zwash.common.grpc.UserServiceProto.UserResponse;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GrpcUserClient {
	private final UserServiceGrpc.UserServiceBlockingStub userStub;

	public GrpcUserClient(@Qualifier("userChannel") ManagedChannel channel) {
		this.userStub = UserServiceGrpc.newBlockingStub(channel);
	}

	public UserResponse getUserFromToken(String token) {
		TokenRequest request = TokenRequest.newBuilder().setToken(token).build();
		return userStub.getUserFromToken(request);
	}
}