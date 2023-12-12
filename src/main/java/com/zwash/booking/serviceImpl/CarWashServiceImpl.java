package com.zwash.booking.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zwash.booking.pojos.Booking;
import com.zwash.booking.pojos.Wash;
import com.zwash.booking.pojos.WashStatus;
import com.zwash.booking.repository.BookingRepository;
import com.zwash.booking.service.CarWashService;
import com.zwash.booking.service.WashService;

import jakarta.transaction.Transactional;

@Service
public class CarWashServiceImpl implements CarWashService {

     @Autowired
	 BookingRepository bookingRepository;

     @Autowired
     WashService washService;

	@Override
	@Transactional
	public void executeCarWash(Booking booking) {

		bookingRepository.executeWash(booking.getId());
		Wash wash = new Wash();
		wash.setBooking(booking);
		wash.setStatus(WashStatus.QUEUING);
		washService.startWash(wash);

        washService.finishWash(wash);
	}

}
