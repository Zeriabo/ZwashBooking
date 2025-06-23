package com.zwash.booking.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zwash.booking.exceptions.StationNotExistsException;
import com.zwash.common.dto.StationDTO;
import com.zwash.common.pojos.CarWashingProgram;
import com.zwash.common.pojos.Media;
import com.zwash.common.pojos.Station;



@Service
public interface StationService extends Serializable {

	Station getStation(Long id) throws StationNotExistsException;

	List<Station> getAllStations();

	List<Station> getAllServiceProviderStations(Long id);

	void setMedia(Long id, Media media);

	void setAddress(Long id, Long latitude, Long longitude);

	Station createStation(StationDTO stationDTO) throws Exception;

	Station createStation(StationDTO stationDTO,Long serviceProviderId) throws Exception;

	Station createStation(Station stationInput) throws Exception;

	Station updateStation(Station station) throws StationNotExistsException;

	void removeStation(Long id)  throws StationNotExistsException;

	List<CarWashingProgram> getStationWashed(Long id ) throws StationNotExistsException;



}
