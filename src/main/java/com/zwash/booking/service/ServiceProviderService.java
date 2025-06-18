package com.zwash.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zwash.booking.exceptions.ServiceProviderNotExistsException;
import com.zwash.booking.exceptions.StationNotExistsException;
import com.zwash.common.pojos.ServiceProvider;
import com.zwash.common.pojos.Station;

@Service
public interface ServiceProviderService {

	ServiceProvider getServiceProvider(Long id) throws ServiceProviderNotExistsException;

	List<ServiceProvider> getAllServiceProviders();

	void setEmail(Long id,String email) throws ServiceProviderNotExistsException;

	boolean addStation(Long id,Station stationDTO) throws StationNotExistsException, ServiceProviderNotExistsException;

	void removeStation(Long id,Station stationDTO)  throws StationNotExistsException, ServiceProviderNotExistsException;

	List<ServiceProvider> getAllServiceProviders(String username);

	ServiceProvider createServiceProvicer(ServiceProvider serviceProvider);
}
