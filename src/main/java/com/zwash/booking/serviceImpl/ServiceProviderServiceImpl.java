package com.zwash.booking.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zwash.booking.exceptions.ServiceProviderNotExistsException;
import com.zwash.booking.exceptions.StationNotExistsException;
import com.zwash.booking.pojos.ServiceProvider;
import com.zwash.booking.pojos.Station;
import com.zwash.booking.repository.ServiceProviderRepository;
import com.zwash.booking.repository.StationRepository;
import com.zwash.booking.service.ServiceProviderService;


@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

	 private final ServiceProviderRepository serviceProviderRepository;

	 private final StationRepository stationRepository;

	    @Autowired
	    public ServiceProviderServiceImpl(ServiceProviderRepository serviceProviderRepository, StationRepository stationRepository) {
	        this.serviceProviderRepository = serviceProviderRepository;
	        this.stationRepository = stationRepository;
	    }

	    @Override
	    public ServiceProvider getServiceProvider(Long id) throws ServiceProviderNotExistsException {
	        return serviceProviderRepository.findById(id).orElseThrow(() -> new ServiceProviderNotExistsException("ServiceProvider not found."));
	    }

	@Override
	public List<ServiceProvider> getAllServiceProviders() {
		 return serviceProviderRepository.findAll();
	}

	@Override
	public void setEmail(Long id, String email) throws ServiceProviderNotExistsException {
		   ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
	                .orElseThrow(() -> new ServiceProviderNotExistsException("ServiceProvider not found."));
	        serviceProvider.setEmail(email);
	        serviceProviderRepository.save(serviceProvider);

	}



	@Override
	public boolean addStation(Long id, Station stationDTO) throws StationNotExistsException, ServiceProviderNotExistsException {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new ServiceProviderNotExistsException("ServiceProvider not found."));

        Station station = stationRepository.findById(stationDTO.getId())
                .orElseThrow(() -> new StationNotExistsException("Station not found."));

        // Add the station to the serviceProvider's list of stations
        serviceProvider.getStations().add(station);

        // Set the serviceProvider for the station
        station.setServiceProvider(serviceProvider);

        // Save both the serviceProvider and the station
        serviceProviderRepository.save(serviceProvider);
        stationRepository.save(station);

        return true;
	}

	@Override
	public void removeStation(Long id, Station stationDTO) throws StationNotExistsException, ServiceProviderNotExistsException {
		  ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
	                .orElseThrow(() -> new ServiceProviderNotExistsException("ServiceProvider not found."));

	        Station station = stationRepository.findById(stationDTO.getId())
	                .orElseThrow(() -> new StationNotExistsException("Station not found."));

	        // Remove the station from the serviceProvider's list of stations
	        serviceProvider.getStations().remove(station);

	        // Set the serviceProvider to null for the station
	        station.setServiceProvider(null);

	        // Save both the serviceProvider and the station
	        serviceProviderRepository.save(serviceProvider);
	        stationRepository.save(station);

	}

	@Override
	public List<ServiceProvider> getAllServiceProviders(String username) {

		 return serviceProviderRepository.findbyUserName(username);
	}

	@Override
	public ServiceProvider createServiceProvicer(ServiceProvider serviceProvider) {

		return serviceProviderRepository.save(serviceProvider);
	}

}
