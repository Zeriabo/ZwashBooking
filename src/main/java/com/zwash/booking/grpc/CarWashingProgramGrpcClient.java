package com.zwash.booking.grpc;

import org.springframework.stereotype.Service;

import com.zwash.common.grpc.CarWashingProgram.CarWashingProgramResponse;
import com.zwash.common.grpc.CarWashingProgram.GetWashingProgramRequest;
import com.zwash.common.grpc.CarWashingProgramServiceGrpc;
import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.FoamCarWashingProgram;
import com.zwash.common.pojos.HighPressureCarWashingProgram;
import com.zwash.common.pojos.TouchlessCarWashingProgram;

@Service
public class CarWashingProgramGrpcClient {
	private final CarWashingProgramServiceGrpc.CarWashingProgramServiceBlockingStub stub;

	public CarWashingProgramGrpcClient(CarWashingProgramServiceGrpc.CarWashingProgramServiceBlockingStub stub) {
		this.stub = stub;
	}

	public CarWashingProgram getProgramById(long id) {
		GetWashingProgramRequest request = GetWashingProgramRequest.newBuilder().setId(id).build();
		CarWashingProgramResponse response = stub.getProgramById(request);
		CarWashingProgram program;
		switch (response.getProgramType()) {
		case "HighPressure":
			HighPressureCarWashingProgram high = new HighPressureCarWashingProgram();
			high.setWaterPressure(100);
			program = high;
			break;
		case "Foam":
			FoamCarWashingProgram foam = new FoamCarWashingProgram();
			foam.setWaterPressure(80);
			foam.setSoapAmount(50);
			foam.setBrushType("Soft");
			program = foam;
			break;
		case "Touchless":
			TouchlessCarWashingProgram touchless = new TouchlessCarWashingProgram();
			touchless.setWaterPressure(70);
			touchless.setSoapAmount(40);
			program = touchless;
			break;
		default:
			throw new IllegalArgumentException("Unknown program type: " + response.getProgramType());
		}
		program.setId(response.getId());
		program.setDescription(response.getDescription());
		program.setPrice(response.getPrice());
		return program;
	}
}