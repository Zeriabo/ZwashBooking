package com.zwash.booking.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zwash.booking.dto.CarWashingProgramDTO;
import com.zwash.booking.dto.StationDTO;
import com.zwash.booking.exceptions.ServiceProviderNotExistsException;
import com.zwash.booking.exceptions.StationNotExistsException;
import com.zwash.booking.pojos.CarWashingProgram;
import com.zwash.booking.pojos.FoamCarWashingProgram;
import com.zwash.booking.pojos.HighPressureCarWashingProgram;
import com.zwash.booking.pojos.Media;
import com.zwash.booking.pojos.ServiceProvider;
import com.zwash.booking.pojos.Station;
import com.zwash.booking.pojos.TouchlessCarWashingProgram;
import com.zwash.booking.repository.CarWashingProgramRepository;
import com.zwash.booking.repository.StationRepository;
import com.zwash.booking.service.ServiceProviderService;
import com.zwash.booking.service.StationService;

import jakarta.transaction.Transactional;

@SuppressWarnings("serial")
@Service
public class StationServiceImpl implements StationService {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private CarWashingProgramRepository carWashingProgramRepository;

	@Autowired
	private ServiceProviderService serviceProviderService;

	@Override
	public Station getStation(Long id) throws StationNotExistsException {

		try {
			Station station = stationRepository.findById(id).get();
			return station;
		} catch (NoSuchElementException noSuchElementException) {
			throw new StationNotExistsException(id);
		} catch (Exception ex) {
			throw ex;
		}

	}

	@Override
	public List<Station> getAllServiceProviderStations(Long id) {
		try {

			List<Station> station = stationRepository.findByServiceProvider(id);
			return station;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	@Transactional
	public void setMedia(Long id, Media media) {
		stationRepository.setMedia(id, media);

	}

	@Override
	@Transactional
	public void setAddress(Long id, Long latitude, Long longitude) {

		stationRepository.setAddress(id, latitude, longitude);

	}

	@Override
	public List<Station> getAllStations() {

		return stationRepository.findAll();
	}
	@Override
	public Station createStation(Station stationRequest) throws Exception {
		Station station = new Station();
		station.setName(stationRequest.getName());
		station.setLatitude(stationRequest.getLatitude());
		station.setLongitude(stationRequest.getLongitude());
		station.setAddress(stationRequest.getAddress());
		// Set the programs (CarWashingPrograms) for the station
		List<CarWashingProgram> programs = new ArrayList<>();
		if (stationRequest.getPrograms() != null) {
			for (CarWashingProgram programRequestDTO : stationRequest.getPrograms()) {
				CarWashingProgram program;
				if (programRequestDTO.getProgramType().equals("high_pressure")) {
					program = new HighPressureCarWashingProgram();
					// additional properties needs to set
				} else if (programRequestDTO.getProgramType().equals("foam")) {
					program = new FoamCarWashingProgram();
					// additional properties needs to set
				} else if (programRequestDTO.getProgramType().equals("touch_less")) {
					program = new TouchlessCarWashingProgram();
					// additional properties needs to set
				} else {
					throw new Exception(programRequestDTO.getProgramType());
				}
				carWashingProgramRepository.save(program);
				programs.add(program);
			}
		}
		station.setPrograms(programs);

		return stationRepository.save(station);
	}

	@Override
	public Station createStation(StationDTO stationRequestDTO) throws Exception {
		Station station = new Station();
		station.setName(stationRequestDTO.getName());
		station.setLatitude(stationRequestDTO.getLatitude());
		station.setLongitude(stationRequestDTO.getLongitude());
		station.setAddress(stationRequestDTO.getAddress());
		// Set the programs (CarWashingPrograms) for the station
		List<CarWashingProgram> programs = new ArrayList<>();
		if (stationRequestDTO.getPrograms() != null) {
			for (CarWashingProgramDTO programRequestDTO : stationRequestDTO.getPrograms()) {
				CarWashingProgram program;
				if (programRequestDTO.getProgramType().equals("high_pressure")) {
					program = new HighPressureCarWashingProgram();
					// additional properties needs to set
				} else if (programRequestDTO.getProgramType().equals("foam")) {
					program = new FoamCarWashingProgram();
					// additional properties needs to set
				} else if (programRequestDTO.getProgramType().equals("touch_less")) {
					program = new TouchlessCarWashingProgram();
					// additional properties needs to set
				} else {
					throw new Exception(programRequestDTO.getProgramType());
				}
				carWashingProgramRepository.save(program);
				programs.add(program);
			}
		}
		station.setPrograms(programs);

		return stationRepository.save(station);
	}
	 @Override
	public Station createStation(StationDTO stationDTO,Long serviceProviderId) throws  ServiceProviderNotExistsException ,Exception{

		ServiceProvider serviceProvider = serviceProviderService.getServiceProvider(serviceProviderId);

		if(serviceProvider != null)
		{
			Station station = new Station();
			station.setName(stationDTO.getName());
			station.setLatitude(stationDTO.getLatitude());
			station.setLongitude(stationDTO.getLongitude());
			station.setAddress(stationDTO.getAddress());
			station.setServiceProvider(serviceProvider);
			// Set the programs (CarWashingPrograms) for the station
			List<CarWashingProgram> programs = new ArrayList<>();
			if (stationDTO.getPrograms() != null) {
				for (CarWashingProgramDTO programRequestDTO : stationDTO.getPrograms()) {
					CarWashingProgram program;
					if (programRequestDTO.getProgramType().equals("high_pressure")) {
						program = new HighPressureCarWashingProgram();
						// additional properties needs to set
					} else if (programRequestDTO.getProgramType().equals("foam")) {
						program = new FoamCarWashingProgram();
						// additional properties needs to set
					} else if (programRequestDTO.getProgramType().equals("touch_less")) {
						program = new TouchlessCarWashingProgram();
						// additional properties needs to set
					} else {
						throw new Exception(programRequestDTO.getProgramType());
					}
					carWashingProgramRepository.save(program);
					programs.add(program);
				}
			}
			station.setPrograms(programs);

			return stationRepository.save(station);
		}else {
			throw new ServiceProviderNotExistsException("Service Provider with id: "+serviceProviderId+" does not exists!");
		}
	 }
	@Override
	public Station updateStation(Station station) throws StationNotExistsException {
		// TODO Auto-generated method stub
		Station existingStation = getStation(station.getId());

		// Update the station properties
		existingStation.setName(station.getName());
		existingStation.setLatitude(station.getLatitude());
		existingStation.setLongitude(station.getLongitude());
        existingStation.setAddress(station.getAddress());
		existingStation.setPrograms(station.getPrograms());

		// Save the updated station
		return stationRepository.save(existingStation);
	}

	@Override
	public void removeStation(Long id) throws StationNotExistsException {
		// Check if the station exists
	    if (!stationRepository.existsById(id)) {
	        throw new StationNotExistsException(id );
	    }

	    // Remove the station
	    stationRepository.deleteById(id);
	}

	@Override
	public List<CarWashingProgram> getStationWashed(Long id) throws StationNotExistsException {
		List<CarWashingProgram> washesList= stationRepository.getWashes(id);

		return washesList;
	}






}
