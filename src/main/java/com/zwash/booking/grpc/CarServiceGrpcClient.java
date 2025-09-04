package com.zwash.booking.grpc;

import org.springframework.stereotype.Service;

import com.zwash.common.car.grpc.CarServiceGrpc;
import com.zwash.common.car.grpc.CarServiceOuterClass;
import com.zwash.common.car.grpc.CarServiceOuterClass.GetCarByPlateRequest;
import com.zwash.common.car.grpc.CarServiceOuterClass.GetCarRequest;
import com.zwash.common.pojos.Car;


@Service
public class CarServiceGrpcClient {
	
	private final com.zwash.common.car.grpc.CarServiceGrpc.CarServiceBlockingStub carStub;

	public CarServiceGrpcClient(CarServiceGrpc.CarServiceBlockingStub carStub) {
		this.carStub = carStub;
	}

	public CarServiceOuterClass.CarResponse getCarById(long id) {
		GetCarRequest request = GetCarRequest.newBuilder().setId(id).build();
		return carStub.getCarById(request);
	}

	public CarServiceOuterClass.CarResponse getCarByPlate(String plate) {
		GetCarByPlateRequest request = GetCarByPlateRequest.newBuilder().setRegistrationPlate(plate).build();
		return carStub.getCarByPlate(request);
	}

	public Car getCar(Long id) throws Exception {
		CarServiceOuterClass.CarResponse response = getCarById(id);
		Car car = new Car();
		car.setCarId(response.getId());
		car.setManufacture(response.getModel());
		car.setRegistrationPlate(response.getRegistrationPlate());
		return car;
	}
}