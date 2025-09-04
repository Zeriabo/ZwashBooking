package com.zwash.booking.grpc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.zwash.common.grpc.StationProto.StationRequest;
import com.zwash.common.grpc.StationProto.StationResponse;
import com.zwash.common.grpc.StationServiceGrpc;
import com.zwash.common.pojos.Station;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Component
public class GrpcStationClient {
	private final StationServiceGrpc.StationServiceBlockingStub stub;

	public GrpcStationClient(@Qualifier("stationChannel") ManagedChannel channel) {
		this.stub = StationServiceGrpc.newBlockingStub(channel);
	}

	public Station getStation(Long id) {
		StationRequest request = StationRequest.newBuilder().setId(id).build();
		StationResponse response = stub.getStation(request);
		Station station = new Station();
		station.setId(response.getId());
		station.setName(response.getName());
		station.setLatitude(response.getLatitude());
		station.setLongitude(response.getLongitude());
		station.setAddress(response.getAddress());
		return station;
	}
}