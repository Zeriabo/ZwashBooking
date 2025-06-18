package com.zwash.booking.service;

import org.springframework.stereotype.Service;

import com.zwash.common.pojos.Booking;



@Service
public interface CarWashService {

	void executeCarWash(Booking booking);
}
