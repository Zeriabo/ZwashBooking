package com.zwash.booking.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.zwash.booking.pojos.CarWashingProgram;
import com.zwash.booking.pojos.Station;
import com.zwash.booking.service.CarWashingProgramService;



@Service
public class CarWashingProgramServiceImpl implements CarWashingProgramService {

	@Autowired
	private CarWashingProgramRepository programRepository;

	@Autowired
	private StationService stationService;

	@Override
	public CarWashingProgram createProgram(CarWashingProgram program) throws Exception, ProgramAlreadyExistsException {
		List<CarWashingProgram> listprograms;
		Station station;

		try {
			station = program.getStation();
			station = stationService.getStation(station.getId());
			program.setStation(station);
			program.setProgramType(program.getClass().getSimpleName());

			listprograms = station.getPrograms();
		} catch (StationNotExistsException stationNotExistsException) {
			throw new StationNotExistsException(program.getStation().getId());
		}
		// Check if a program with the same id exists in the list
		boolean programExists = false;
		for (CarWashingProgram existingProgram : listprograms) {
			if (existingProgram.getProgramType().equals(program.getProgramType())) {
				programExists = true;
				break;
			}
		}

		if (programExists) {
			// Handle the case when the program already exists
			// For example, throw an exception or return an error message
			throw new ProgramAlreadyExistsException("Program already exists");
		} else {
			listprograms.add(program);
		}
		station.setPrograms(listprograms);

		stationService.updateStation(station);
		return program;
		// return programRepository.save(program);
	}

	@Override
	public CarWashingProgram getProgramById(Long id) {
		return programRepository.findById(id).orElse(null);
	
	}

	@Override
	public void updateProgram(CarWashingProgram program) {
		programRepository.save(program);
	}

	@Override
	public void deleteProgram(Long id) {
		programRepository.deleteById(id);
	}

	@Override
	public List<CarWashingProgram> getPrograms(@Param("stationId")Long stationId) {

		return programRepository.findByStationId(stationId);

	}

	@Override
	public List<CarWashingProgram> getPrograms() {

		return programRepository.findAll();

	}

	@Override
	public CarWashingProgram getCarWashProgramById(Long carWashProgramId) {
		
		return programRepository.getReferenceById(carWashProgramId);
	}

}
