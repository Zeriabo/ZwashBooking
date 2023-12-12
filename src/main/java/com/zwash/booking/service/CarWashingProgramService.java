package com.zwash.booking.service;

import java.util.List;

import com.zwash.booking.pojos.CarWashingProgram;

public interface CarWashingProgramService {

    public CarWashingProgram createProgram(CarWashingProgram program) throws  Exception;

    public CarWashingProgram getProgramById(Long id);

    public void updateProgram(CarWashingProgram program);

    public void deleteProgram(Long id);

	public List<CarWashingProgram> getPrograms(Long stationId);

	List<CarWashingProgram> getPrograms();

	public CarWashingProgram getCarWashProgramById(Long carWashProgramId);

}
