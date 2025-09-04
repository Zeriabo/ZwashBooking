package com.zwash.booking.util;
import com.zwash.common.grpc.UserServiceProto.UserResponse;
import com.zwash.common.pojos.User;
public class Utility {

	public static User mapGrpcUser(UserResponse grpcUser) {
	    User user = new User();
	    user.setId(grpcUser.getId());
	    user.setFirstName(grpcUser.getFirstName());
	    user.setLastName(grpcUser.getLastName());
	    user.setUsername(grpcUser.getUsername());
	    user.setToken(grpcUser.getToken());
	    // Map other fields if needed
	    return user;
	}
}
